package com.alligator.market.backend.sourceplan.plan.persistence.jooq.registry;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlan;
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

import static com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlan.EntryLifecycleStatus.AVAILABLE;
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
    public Optional<SourcePlan> findAvailableByKey(SourcePlanKey key) {
        Objects.requireNonNull(key, "key must not be null");

        List<PrioritizedSourceCode> prioritizedSourceCodes = selectAvailableSourceCodes(
                SOURCE_PLAN_CAPTURER_CODE.eq(key.capturerCode().value())
                        .and(SOURCE_PLAN_INSTRUMENT_CODE.eq(key.instrumentCode().value()))
        ).get(key);

        if (prioritizedSourceCodes == null || prioritizedSourceCodes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SourcePlan(key, prioritizedSourceCodes));
    }

    @Override
    public List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return toPlans(selectAvailableSourceCodes(SOURCE_PLAN_CAPTURER_CODE.eq(capturerCode.value())));
    }

    @Override
    public List<SourcePlan> findAllAvailable() {
        return toPlans(selectAvailableSourceCodes(null));
    }

    private Map<SourcePlanKey, List<PrioritizedSourceCode>> selectAvailableSourceCodes(Condition additionalCondition) {
        Condition condition = SOURCE_PLAN_EXECUTION_STATUS.eq(
                        StoredSourcePlan.ExecutionStatus.AVAILABLE.name())
                .and(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(AVAILABLE.name()));

        if (additionalCondition != null) {
            condition = condition.and(additionalCondition);
        }

        Map<SourcePlanKey, List<PrioritizedSourceCode>> groupedSourceCodes = new LinkedHashMap<>();

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

                    PrioritizedSourceCode prioritizedSourceCode = new PrioritizedSourceCode(
                            new SourceCode(record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE)),
                            record.get(SOURCE_PLAN_ENTRY_PRIORITY)
                    );

                    groupedSourceCodes
                            .computeIfAbsent(planKey, ignored -> new ArrayList<>())
                            .add(prioritizedSourceCode);
                });

        return groupedSourceCodes;
    }

    private static List<SourcePlan> toPlans(Map<SourcePlanKey, List<PrioritizedSourceCode>> groupedSourceCodes) {
        List<SourcePlan> plans = new ArrayList<>(groupedSourceCodes.size());

        for (Map.Entry<SourcePlanKey, List<PrioritizedSourceCode>> groupedSourceCode : groupedSourceCodes.entrySet()) {
            SourcePlanKey planKey = groupedSourceCode.getKey();
            plans.add(new SourcePlan(
                    planKey,
                    groupedSourceCode.getValue()
            ));
        }

        return List.copyOf(plans);
    }
}
