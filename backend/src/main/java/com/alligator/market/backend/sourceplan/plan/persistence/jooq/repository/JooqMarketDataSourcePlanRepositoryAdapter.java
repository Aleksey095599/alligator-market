package com.alligator.market.backend.sourceplan.plan.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataCaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePlan.MARKET_DATA_SOURCE_PLAN;

/**
 * jOOQ adapter for source plan persistence.
 */
public final class JooqMarketDataSourcePlanRepositoryAdapter implements MarketDataSourcePlanRepository {

    /* Foreign key from market_data_source_plan to instrument_registry by instrument code. */
    private static final String FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT =
            "fk_market_data_source_plan_instrument";
    /* Foreign key from market_data_source_plan to capture_process_passport by capture process code. */
    private static final String FK_MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS =
            "fk_market_data_source_plan_capture_process";
    private static final Field<String> MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE =
            MARKET_DATA_SOURCE.COLLECTION_PROCESS_CODE;
    private static final Field<String> MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE =
            MARKET_DATA_SOURCE_PLAN.COLLECTION_PROCESS_CODE;

    private final DSLContext dsl;

    public JooqMarketDataSourcePlanRepositoryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<MarketDataSourcePlan> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        return findByMarketDataCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode, false);
    }

    @Override
    public Optional<MarketDataSourcePlan> findActiveByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        return findByMarketDataCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode, true);
    }

    private Optional<MarketDataSourcePlan> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode,
            boolean activeOnly
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        Condition condition = MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(captureProcessCode.value())
                .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()));

        if (activeOnly) {
            condition = condition.and(MARKET_DATA_SOURCE.LIFECYCLE_STATUS.eq(ACTIVE.name()));
        }

        // Read plan entries in their capture priority order.
        List<MarketDataSourcePlanEntry> entries = dsl
                .select(
                        MARKET_DATA_SOURCE.SOURCE_CODE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .where(condition)
                .orderBy(MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> toEntry(
                        record.get(MARKET_DATA_SOURCE.SOURCE_CODE),
                        record.get(MARKET_DATA_SOURCE.PRIORITY)
                ));

        // A plan without source entries is treated as absent.
        if (entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new MarketDataSourcePlan(captureProcessCode, instrumentCode, entries));
    }

    @Override
    public List<MarketDataSourcePlan> findAll() {
        // Group market_data_source rows by source plan identity.
        Map<PlanKey, List<MarketDataSourcePlanEntry>> groupedEntries = new LinkedHashMap<>();

        dsl.select(
                        MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE,
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE,
                        MARKET_DATA_SOURCE.SOURCE_CODE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .orderBy(
                        MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.asc(),
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE.asc(),
                        MARKET_DATA_SOURCE.PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    PlanKey planKey = new PlanKey(
                            new MarketDataCaptureProcessCode(record.get(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE)),
                            new InstrumentCode(record.get(MARKET_DATA_SOURCE.INSTRUMENT_CODE))
                    );

                    MarketDataSourcePlanEntry entry = toEntry(
                            record.get(MARKET_DATA_SOURCE.SOURCE_CODE),
                            record.get(MARKET_DATA_SOURCE.PRIORITY)
                    );

                    groupedEntries
                            .computeIfAbsent(planKey, ignored -> new ArrayList<>())
                            .add(entry);
                });

        List<MarketDataSourcePlan> plans = new ArrayList<>(groupedEntries.size());

        for (Map.Entry<PlanKey, List<MarketDataSourcePlanEntry>> groupedEntry : groupedEntries.entrySet()) {
            PlanKey planKey = groupedEntry.getKey();
            plans.add(new MarketDataSourcePlan(
                    planKey.captureProcessCode(),
                    planKey.instrumentCode(),
                    groupedEntry.getValue()
            ));
        }

        return List.copyOf(plans);
    }

    @Override
    public boolean createIfAbsent(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        try {
            return dsl.transactionResult(configuration -> {
                DSLContext tx = configuration.dsl();

                int insertedPlans = tx.insertInto(MARKET_DATA_SOURCE_PLAN)
                        .set(MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE, plan.captureProcessCode().value())
                        .set(MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE, plan.instrumentCode().value())
                        .onConflict(
                                MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE,
                                MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE
                        )
                        .doNothing()
                        .execute();

                if (insertedPlans == 0) {
                    return false;
                }

                for (MarketDataSourcePlanEntry entry : plan.entries()) {
                    insertEntry(tx, plan.captureProcessCode(), plan.instrumentCode(), entry);
                }

                return true;
            });
        } catch (DataIntegrityViolationException ex) {
            if (DbConstraintErrors.isViolationOf(ex, FK_MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS)) {
                throw new MarketDataCaptureProcessCodeNotFoundException(plan.captureProcessCode());
            }

            if (DbConstraintErrors.isViolationOf(ex, FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT)) {
                throw new InstrumentCodeNotFoundException(plan.instrumentCode());
            }

            throw ex;
        }
    }


    @Override
    public boolean replaceIfExists(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        return dsl.transactionResult(configuration -> {
            DSLContext tx = configuration.dsl();

            // Lock the plan row so the replacement is atomic for this plan identity.
            boolean planExists = tx.selectOne()
                    .from(MARKET_DATA_SOURCE_PLAN)
                    .where(MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE.eq(plan.captureProcessCode().value()))
                    .and(MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .forUpdate()
                    .fetchOptional()
                    .isPresent();

            if (!planExists) {
                return false;
            }

            tx.deleteFrom(MARKET_DATA_SOURCE)
                    .where(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(plan.captureProcessCode().value()))
                    .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .execute();

            for (MarketDataSourcePlanEntry entry : plan.entries()) {
                insertEntry(tx, plan.captureProcessCode(), plan.instrumentCode(), entry);
            }

            return true;
        });
    }

    @Override
    public boolean deleteIfExistsByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Source entries are removed by the database cascade.
        int deletedRows = dsl.deleteFrom(MARKET_DATA_SOURCE_PLAN)
                .where(MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE.eq(captureProcessCode.value()))
                .and(MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();

        return deletedRows > 0;
    }

    private void insertEntry(
            DSLContext dsl,
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode,
            MarketDataSourcePlanEntry entry
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(entry, "entry must not be null");

        dsl.insertInto(MARKET_DATA_SOURCE)
                .set(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE, captureProcessCode.value())
                .set(MARKET_DATA_SOURCE.INSTRUMENT_CODE, instrumentCode.value())
                .set(MARKET_DATA_SOURCE.SOURCE_CODE, entry.sourceCode().value())
                .set(MARKET_DATA_SOURCE.PRIORITY, entry.priority())
                .set(MARKET_DATA_SOURCE.LIFECYCLE_STATUS, ACTIVE.name())
                .execute();
    }

    private MarketDataSourcePlanEntry toEntry(
            String sourceCode,
            Integer priority
    ) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(priority, "priority must not be null");

        return new MarketDataSourcePlanEntry(
                new MarketDataSourceCode(sourceCode),
                priority
        );
    }

    private record PlanKey(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
    }
}
