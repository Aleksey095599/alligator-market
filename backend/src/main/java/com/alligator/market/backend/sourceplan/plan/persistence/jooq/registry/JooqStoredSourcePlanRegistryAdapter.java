package com.alligator.market.backend.sourceplan.plan.persistence.jooq.registry;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanEntryLifecycleStatus.ACTIVE;
import static com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus.EXECUTABLE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqStoredSourcePlanRegistryAdapter implements StoredSourcePlanRegistry {
    private static final Table<?> SOURCE_PLAN = table(name("source_plan"));
    private static final Table<?> SOURCE_PLAN_ENTRY = table(name("source_plan_entry"));
    private static final Field<String> SOURCE_PLAN_CAPTURER_CODE =
            field(name("source_plan", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_INSTRUMENT_CODE =
            field(name("source_plan", "instrument_code"), String.class);
    private static final Field<String> SOURCE_PLAN_EXECUTION_STATUS =
            field(name("source_plan", "execution_status"), String.class);
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

    private final DSLContext dsl;

    public JooqStoredSourcePlanRegistryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<SourcePlan> findExecutableByKey(SourcePlanKey key) {
        Objects.requireNonNull(key, "key must not be null");

        List<SourcePlanEntry> entries = selectExecutableEntries(
                SOURCE_PLAN_CAPTURER_CODE.eq(key.capturerCode().value())
                        .and(SOURCE_PLAN_INSTRUMENT_CODE.eq(key.instrumentCode().value()))
        ).get(key);

        if (entries == null || entries.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SourcePlan(key.capturerCode(), key.instrumentCode(), entries));
    }

    @Override
    public List<SourcePlan> findExecutableByCapturerCode(CapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return toPlans(selectExecutableEntries(SOURCE_PLAN_CAPTURER_CODE.eq(capturerCode.value())));
    }

    @Override
    public List<SourcePlan> findAllExecutable() {
        return toPlans(selectExecutableEntries(null));
    }

    private Map<SourcePlanKey, List<SourcePlanEntry>> selectExecutableEntries(Condition additionalCondition) {
        Condition condition = SOURCE_PLAN_EXECUTION_STATUS.eq(EXECUTABLE.name())
                .and(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(ACTIVE.name()));

        if (additionalCondition != null) {
            condition = condition.and(additionalCondition);
        }

        Map<SourcePlanKey, List<SourcePlanEntry>> groupedEntries = new LinkedHashMap<>();

        dsl.select(
                        SOURCE_PLAN_ENTRY_CAPTURER_CODE,
                        SOURCE_PLAN_ENTRY_INSTRUMENT_CODE,
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY
                )
                .from(SOURCE_PLAN)
                .join(SOURCE_PLAN_ENTRY)
                .on(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                .where(condition)
                .orderBy(
                        SOURCE_PLAN_ENTRY_CAPTURER_CODE.asc(),
                        SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.asc(),
                        SOURCE_PLAN_ENTRY_PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    SourcePlanKey planKey = new SourcePlanKey(
                            new CapturerCode(record.get(SOURCE_PLAN_ENTRY_CAPTURER_CODE)),
                            new InstrumentCode(record.get(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE))
                    );

                    SourcePlanEntry entry = new SourcePlanEntry(
                            new SourceCode(record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE)),
                            record.get(SOURCE_PLAN_ENTRY_PRIORITY)
                    );

                    groupedEntries
                            .computeIfAbsent(planKey, ignored -> new ArrayList<>())
                            .add(entry);
                });

        return groupedEntries;
    }

    private static List<SourcePlan> toPlans(Map<SourcePlanKey, List<SourcePlanEntry>> groupedEntries) {
        List<SourcePlan> plans = new ArrayList<>(groupedEntries.size());

        for (Map.Entry<SourcePlanKey, List<SourcePlanEntry>> groupedEntry : groupedEntries.entrySet()) {
            SourcePlanKey planKey = groupedEntry.getKey();
            plans.add(new SourcePlan(
                    planKey.capturerCode(),
                    planKey.instrumentCode(),
                    groupedEntry.getValue()
            ));
        }

        return List.copyOf(plans);
    }
}
