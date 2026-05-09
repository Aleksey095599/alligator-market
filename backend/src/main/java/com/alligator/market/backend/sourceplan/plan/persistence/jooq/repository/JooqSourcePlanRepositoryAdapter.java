package com.alligator.market.backend.sourceplan.plan.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataCapturerCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static com.alligator.market.backend.sourceplan.plan.persistence.SourcePlanEntryLifecycleStatus.ACTIVE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqSourcePlanRepositoryAdapter implements SourcePlanRepository {
    private static final Table<?> SOURCE_PLAN = table(name("source_plan"));
    private static final Table<?> SOURCE_PLAN_ENTRY = table(name("source_plan_entry"));

    // Keep these names aligned with the database constraint names used in migrations.
    private static final String FK_SOURCE_PLAN_INSTRUMENT =
            "fk_source_plan_instrument";

    private static final String FK_SOURCE_PLAN_CAPTURER =
            "fk_source_plan_capturer";
    private static final Field<String> SOURCE_PLAN_ENTRY_CAPTURER_CODE =
            field(name("source_plan_entry", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_INSTRUMENT_CODE =
            field(name("source_plan_entry", "instrument_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_SOURCE_CODE =
            field(name("source_plan_entry", "source_code"), String.class);
    private static final Field<Integer> SOURCE_PLAN_ENTRY_PRIORITY =
            field(name("source_plan_entry", "priority"), Integer.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS =
            field(name("source_plan_entry", "lifecycle_status"), String.class);
    private static final Field<String> SOURCE_PLAN_CAPTURER_CODE =
            field(name("source_plan", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_INSTRUMENT_CODE =
            field(name("source_plan", "instrument_code"), String.class);

    private final DSLContext dsl;

    public JooqSourcePlanRepositoryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<SourcePlan> findByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        return findByMarketDataCapturerCodeAndInstrumentCode(capturerCode, instrumentCode, false);
    }

    @Override
    public Optional<SourcePlan> findExecutableByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        return findByMarketDataCapturerCodeAndInstrumentCode(capturerCode, instrumentCode, true);
    }

    private Optional<SourcePlan> findByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode,
            boolean executableOnly
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        Condition condition = SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(capturerCode.value())
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(instrumentCode.value()));

        if (executableOnly) {
            condition = condition.and(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(ACTIVE.name()));
        }

        List<SourcePlanEntry> entries = dsl
                .select(
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY
                )
                .from(SOURCE_PLAN_ENTRY)
                .where(condition)
                .orderBy(SOURCE_PLAN_ENTRY_PRIORITY.asc())
                .fetch(record -> toEntry(
                        record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE),
                        record.get(SOURCE_PLAN_ENTRY_PRIORITY)
                ));

        // A plan with no source entries is not usable for capture, so it is treated as absent.
        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SourcePlan(capturerCode, instrumentCode, entries));
    }

    @Override
    public List<SourcePlan> findAll() {
        Map<PlanKey, List<SourcePlanEntry>> groupedEntries = new LinkedHashMap<>();

        dsl.select(
                        SOURCE_PLAN_ENTRY_CAPTURER_CODE,
                        SOURCE_PLAN_ENTRY_INSTRUMENT_CODE,
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY
                )
                .from(SOURCE_PLAN_ENTRY)
                .orderBy(
                        SOURCE_PLAN_ENTRY_CAPTURER_CODE.asc(),
                        SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.asc(),
                        SOURCE_PLAN_ENTRY_PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    PlanKey planKey = new PlanKey(
                            new MarketDataCapturerCode(
                                    record.get(SOURCE_PLAN_ENTRY_CAPTURER_CODE)
                            ),
                            new InstrumentCode(record.get(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE))
                    );

                    SourcePlanEntry entry = toEntry(
                            record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE),
                            record.get(SOURCE_PLAN_ENTRY_PRIORITY)
                    );

                    groupedEntries
                            .computeIfAbsent(planKey, ignored -> new ArrayList<>())
                            .add(entry);
                });

        List<SourcePlan> plans = new ArrayList<>(groupedEntries.size());

        for (Map.Entry<PlanKey, List<SourcePlanEntry>> groupedEntry : groupedEntries.entrySet()) {
            PlanKey planKey = groupedEntry.getKey();
            plans.add(new SourcePlan(
                    planKey.capturerCode(),
                    planKey.instrumentCode(),
                    groupedEntry.getValue()
            ));
        }

        return List.copyOf(plans);
    }

    @Override
    public boolean createIfAbsent(SourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        try {
            return dsl.transactionResult(configuration -> {
                DSLContext tx = configuration.dsl();

                int insertedPlans = tx.insertInto(SOURCE_PLAN)
                        .set(SOURCE_PLAN_CAPTURER_CODE, plan.capturerCode().value())
                        .set(SOURCE_PLAN_INSTRUMENT_CODE, plan.instrumentCode().value())
                        .onConflict(
                                SOURCE_PLAN_CAPTURER_CODE,
                                SOURCE_PLAN_INSTRUMENT_CODE
                        )
                        .doNothing()
                        .execute();

                if (insertedPlans == 0) {
                    return false;
                }

                for (SourcePlanEntry entry : plan.entries()) {
                    insertEntry(tx, plan.capturerCode(), plan.instrumentCode(), entry);
                }

                return true;
            });
        } catch (DataIntegrityViolationException ex) {
            if (DbConstraintErrors.isViolationOf(ex, FK_SOURCE_PLAN_CAPTURER)) {
                throw new MarketDataCapturerCodeNotFoundException(plan.capturerCode());
            }

            if (DbConstraintErrors.isViolationOf(ex, FK_SOURCE_PLAN_INSTRUMENT)) {
                throw new InstrumentCodeNotFoundException(plan.instrumentCode());
            }

            throw ex;
        }
    }

    @Override
    public boolean replaceIfExists(SourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        return dsl.transactionResult(configuration -> {
            DSLContext tx = configuration.dsl();

            // The row lock makes delete-and-reinsert replacement atomic for this plan identity.
            boolean planExists = tx.selectOne()
                    .from(SOURCE_PLAN)
                    .where(SOURCE_PLAN_CAPTURER_CODE.eq(plan.capturerCode().value()))
                    .and(SOURCE_PLAN_INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .forUpdate()
                    .fetchOptional()
                    .isPresent();

            if (!planExists) {
                return false;
            }

            tx.deleteFrom(SOURCE_PLAN_ENTRY)
                    .where(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(plan.capturerCode().value()))
                    .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .execute();

            for (SourcePlanEntry entry : plan.entries()) {
                insertEntry(tx, plan.capturerCode(), plan.instrumentCode(), entry);
            }

            return true;
        });
    }

    @Override
    public boolean deleteIfExistsByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Entries are removed by the database cascade from source_plan.
        int deletedRows = dsl.deleteFrom(SOURCE_PLAN)
                .where(SOURCE_PLAN_CAPTURER_CODE.eq(capturerCode.value()))
                .and(SOURCE_PLAN_INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();

        return deletedRows > 0;
    }

    private void insertEntry(
            DSLContext dsl,
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode,
            SourcePlanEntry entry
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(entry, "entry must not be null");

        dsl.insertInto(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_CAPTURER_CODE, capturerCode.value())
                .set(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE, instrumentCode.value())
                .set(SOURCE_PLAN_ENTRY_SOURCE_CODE, entry.sourceCode().value())
                .set(SOURCE_PLAN_ENTRY_PRIORITY, entry.priority())
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, ACTIVE.name())
                .execute();
    }

    private SourcePlanEntry toEntry(
            String sourceCode,
            Integer priority
    ) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(priority, "priority must not be null");

        return new SourcePlanEntry(
                new MarketDataSourceCode(sourceCode),
                priority
        );
    }

    private record PlanKey(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
    }
}
