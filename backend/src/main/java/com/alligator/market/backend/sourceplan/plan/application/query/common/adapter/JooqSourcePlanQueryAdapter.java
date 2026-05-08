package com.alligator.market.backend.sourceplan.plan.application.query.common.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.MarketDataSourceQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

/**
 * Read-side адаптер для административного отображения source plan.
 *
 * <p>Намеренно возвращает и ACTIVE, и RETIRED строки источников: администратор должен видеть
 * устаревшие строки, чтобы удалить или заменить их. Runtime-выбор источника должен идти через
 * доменный репозиторий с фильтрацией по ACTIVE lifecycle status.</p>
 */
public final class JooqSourcePlanQueryAdapter implements SourcePlanQueryPort {

    private static final Table<?> SOURCE_PLAN_ENTRY = table(name("source_plan_entry"));
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
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        Condition condition = SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(capturerCode.value())
                .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(instrumentCode.value()));

        // Без lifecycle-фильтра: edit-форма должна загрузить retired строки, чтобы их можно было удалить.
        List<MarketDataSourceQueryItem> sources = dsl.select(
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY,
                        SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS
                )
                .from(SOURCE_PLAN_ENTRY)
                .where(condition)
                .orderBy(SOURCE_PLAN_ENTRY_PRIORITY.asc())
                .fetch(record -> toSource(
                        record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE),
                        record.get(SOURCE_PLAN_ENTRY_PRIORITY),
                        record.get(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS)
                ));

        if (sources.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SourcePlanQueryItem(
                capturerCode.value(),
                instrumentCode.value(),
                sources
        ));
    }

    @Override
    public List<SourcePlanQueryItem> findAll() {
        // Таблица хранит строки источников, а API возвращает агрегированный план.
        Map<PlanKey, List<MarketDataSourceQueryItem>> groupedSources = new LinkedHashMap<>();

        dsl.select(
                        SOURCE_PLAN_ENTRY_CAPTURER_CODE,
                        SOURCE_PLAN_ENTRY_INSTRUMENT_CODE,
                        SOURCE_PLAN_ENTRY_SOURCE_CODE,
                        SOURCE_PLAN_ENTRY_PRIORITY,
                        SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS
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
                            record.get(SOURCE_PLAN_ENTRY_CAPTURER_CODE),
                            record.get(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE)
                    );

                    MarketDataSourceQueryItem source = toSource(
                            record.get(SOURCE_PLAN_ENTRY_SOURCE_CODE),
                            record.get(SOURCE_PLAN_ENTRY_PRIORITY),
                            record.get(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS)
                    );

                    groupedSources.computeIfAbsent(planKey, ignored -> new ArrayList<>()).add(source);
                });

        List<SourcePlanQueryItem> plans = new ArrayList<>(groupedSources.size());

        for (Map.Entry<PlanKey, List<MarketDataSourceQueryItem>> entry : groupedSources.entrySet()) {
            PlanKey planKey = entry.getKey();
            plans.add(new SourcePlanQueryItem(
                    planKey.capturerCode(),
                    planKey.instrumentCode(),
                    entry.getValue()
            ));
        }

        return List.copyOf(plans);
    }

    private static MarketDataSourceQueryItem toSource(
            String sourceCode,
            Integer priority,
            String lifecycleStatus
    ) {
        Objects.requireNonNull(priority, "priority must not be null");

        return new MarketDataSourceQueryItem(sourceCode, priority, lifecycleStatus);
    }

    private record PlanKey(
            String capturerCode,
            String instrumentCode
    ) {
    }
}
