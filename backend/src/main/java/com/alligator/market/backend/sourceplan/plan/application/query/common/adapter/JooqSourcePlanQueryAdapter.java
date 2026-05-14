package com.alligator.market.backend.sourceplan.plan.application.query.common.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourceQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus;
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

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
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

    private final DSLContext dsl;

    public JooqSourcePlanQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<SourcePlanQueryItem> findByMarketDataCapturerCodeAndInstrumentCode(
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
                        MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS
                )
                .from(SOURCE_PLAN)
                .join(SOURCE_PLAN_ENTRY)
                .on(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                .join(MARKET_DATA_CAPTURER_PASSPORT)
                .on(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
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
                records.getFirst().get(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS),
                StoredSourcePlanExecutionStatus.valueOf(records.getFirst().get(SOURCE_PLAN_EXECUTION_STATUS)),
                instrumentCode.value(),
                sources
        ));
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
                        MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS
                )
                .from(SOURCE_PLAN)
                .join(SOURCE_PLAN_ENTRY)
                .on(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                .join(MARKET_DATA_CAPTURER_PASSPORT)
                .on(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                .orderBy(
                        SOURCE_PLAN_CAPTURER_CODE.asc(),
                        SOURCE_PLAN_INSTRUMENT_CODE.asc(),
                        SOURCE_PLAN_ENTRY_PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    PlanKey planKey = new PlanKey(
                            record.get(SOURCE_PLAN_CAPTURER_CODE),
                            record.get(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS),
                            StoredSourcePlanExecutionStatus.valueOf(record.get(SOURCE_PLAN_EXECUTION_STATUS)),
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
                    planKey.capturerLifecycleStatus(),
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

        return new SourceQueryItem(sourceCode, priority, lifecycleStatus);
    }

    private record PlanKey(
            String capturerCode,
            String capturerLifecycleStatus,
            StoredSourcePlanExecutionStatus executionStatus,
            String instrumentCode
    ) {
    }
}
