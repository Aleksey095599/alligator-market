package com.alligator.market.backend.sourceplan.plan.application.query.common.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourceQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
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

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqSourcePlanQueryAdapter implements SourcePlanQueryPort {
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
    private static final Field<String> CAPTURER_PASSPORT_REGISTRY_STATUS =
            field(name("capturer_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqSourcePlanQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<SourcePlanQueryItem> findByCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        Condition condition = SOURCE_PLAN_CAPTURER_CODE.eq(capturerCode.value())
                .and(SOURCE_PLAN_INSTRUMENT_CODE.eq(instrumentCode.value()));

        var records = dsl.select(
                        SOURCE_PLAN_EXECUTION_STATUS,
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY,
                        SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS,
                        CAPTURER_PASSPORT_REGISTRY_STATUS
                )
                .from(SOURCE_PLAN)
                .join(SOURCE_PLAN_ENTRY)
                .on(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                .join(CAPTURER_PASSPORT)
                .on(CAPTURER_PASSPORT.CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .where(condition)
                .orderBy(SOURCE_PLAN_ENTRY_PRIORITY.asc())
                .fetch();

        if (records.isEmpty()) {
            return Optional.empty();
        }

        List<SourceQueryItem> sources = records.map(record -> toSource(
                        record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE),
                        record.get(SOURCE_PLAN_ENTRY_PRIORITY),
                        record.get(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS)
                ));

        return Optional.of(new SourcePlanQueryItem(
                capturerCode.value(),
                toCapturerPassportRegistryStatus(records.getFirst().get(CAPTURER_PASSPORT_REGISTRY_STATUS)),
                toSourcePlanExecutionStatus(records.getFirst().get(SOURCE_PLAN_EXECUTION_STATUS)),
                instrumentCode.value(),
                sources
        ));
    }

    @Override
    public Map<InstrumentCode, StoredSourcePlan.ExecutionStatus>
            findExecutionStatusesByCapturerCodeAndInstrumentCodes(
                    CapturerCode capturerCode,
                    List<InstrumentCode> instrumentCodes
            ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCodes, "instrumentCodes must not be null");

        List<String> instrumentCodeValues = instrumentCodes.stream()
                .map(instrumentCode -> Objects.requireNonNull(instrumentCode, "instrumentCode must not be null"))
                .map(InstrumentCode::value)
                .distinct()
                .toList();

        if (instrumentCodeValues.isEmpty()) {
            return Map.of();
        }

        Map<InstrumentCode, StoredSourcePlan.ExecutionStatus> statuses = new LinkedHashMap<>();

        dsl.select(SOURCE_PLAN_INSTRUMENT_CODE, SOURCE_PLAN_EXECUTION_STATUS)
                .from(SOURCE_PLAN)
                .where(SOURCE_PLAN_CAPTURER_CODE.eq(capturerCode.value()))
                .and(SOURCE_PLAN_INSTRUMENT_CODE.in(instrumentCodeValues))
                .orderBy(SOURCE_PLAN_INSTRUMENT_CODE.asc())
                .fetch()
                .forEach(record -> statuses.put(
                        new InstrumentCode(record.get(SOURCE_PLAN_INSTRUMENT_CODE)),
                        toSourcePlanExecutionStatus(record.get(SOURCE_PLAN_EXECUTION_STATUS))
                ));

        return Map.copyOf(statuses);
    }

    @Override
    public List<SourcePlanQueryItem> findAll() {
        Map<PlanKey, List<SourceQueryItem>> groupedSources = new LinkedHashMap<>();

        dsl.select(
                        SOURCE_PLAN_CAPTURER_CODE,
                        SOURCE_PLAN_INSTRUMENT_CODE,
                        SOURCE_PLAN_EXECUTION_STATUS,
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY,
                        SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS,
                        CAPTURER_PASSPORT_REGISTRY_STATUS
                )
                .from(SOURCE_PLAN)
                .join(SOURCE_PLAN_ENTRY)
                .on(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                .join(CAPTURER_PASSPORT)
                .on(CAPTURER_PASSPORT.CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .orderBy(
                        SOURCE_PLAN_CAPTURER_CODE.asc(),
                        SOURCE_PLAN_INSTRUMENT_CODE.asc(),
                        SOURCE_PLAN_ENTRY_PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    PlanKey planKey = new PlanKey(
                            record.get(SOURCE_PLAN_CAPTURER_CODE),
                            toCapturerPassportRegistryStatus(record.get(CAPTURER_PASSPORT_REGISTRY_STATUS)),
                            toSourcePlanExecutionStatus(record.get(SOURCE_PLAN_EXECUTION_STATUS)),
                            record.get(SOURCE_PLAN_INSTRUMENT_CODE)
                    );

                    SourceQueryItem source = toSource(
                            record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE),
                            record.get(SOURCE_PLAN_ENTRY_PRIORITY),
                            record.get(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS)
                    );

                    groupedSources.computeIfAbsent(planKey, ignored -> new ArrayList<>()).add(source);
                });

        List<SourcePlanQueryItem> plans = new ArrayList<>(groupedSources.size());

        for (Map.Entry<PlanKey, List<SourceQueryItem>> entry : groupedSources.entrySet()) {
            PlanKey planKey = entry.getKey();
            plans.add(new SourcePlanQueryItem(
                    planKey.capturerCode(),
                    planKey.capturerRegistryStatus(),
                    planKey.executionStatus(),
                    planKey.instrumentCode(),
                    entry.getValue()
            ));
        }

        return List.copyOf(plans);
    }

    private static SourceQueryItem toSource(
            String sourceCode,
            Integer priority,
            String lifecycleStatus
    ) {
        Objects.requireNonNull(priority, "priority must not be null");

        return new SourceQueryItem(
                sourceCode,
                priority,
                StoredSourcePlan.EntryLifecycleStatus.valueOf(lifecycleStatus)
        );
    }

    private static CapturerPassportRecord.RegistryStatus toCapturerPassportRegistryStatus(String status) {
        return CapturerPassportRecord.RegistryStatus.valueOf(status);
    }

    private static StoredSourcePlan.ExecutionStatus toSourcePlanExecutionStatus(String status) {
        return StoredSourcePlan.ExecutionStatus.valueOf(status);
    }

    private record PlanKey(
            String capturerCode,
            CapturerPassportRecord.RegistryStatus capturerRegistryStatus,
            StoredSourcePlan.ExecutionStatus executionStatus,
            String instrumentCode
    ) {
    }
}
