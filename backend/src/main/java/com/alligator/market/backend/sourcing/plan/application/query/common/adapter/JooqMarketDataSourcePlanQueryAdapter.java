package com.alligator.market.backend.sourcing.plan.application.query.common.adapter;

import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourceQueryItem;
import com.alligator.market.backend.sourcing.plan.application.query.common.port.MarketDataSourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;

/**
 * Read-side адаптер для административного отображения source plan.
 *
 * <p>Намеренно возвращает и ACTIVE, и RETIRED строки источников: администратор должен видеть
 * устаревшие строки, чтобы удалить или заменить их. Runtime-выбор источника должен идти через
 * доменный репозиторий с фильтрацией по ACTIVE lifecycle status.</p>
 */
public final class JooqMarketDataSourcePlanQueryAdapter implements MarketDataSourcePlanQueryPort {

    private static final Field<String> MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE =
            MARKET_DATA_SOURCE.COLLECTION_PROCESS_CODE;

    private final DSLContext dsl;

    public JooqMarketDataSourcePlanQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<MarketDataSourcePlanQueryItem> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        Condition condition = MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(captureProcessCode.value())
                .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()));

        // Без lifecycle-фильтра: edit-форма должна загрузить retired строки, чтобы их можно было удалить.
        List<MarketDataSourceQueryItem> sources = dsl.select(
                        MARKET_DATA_SOURCE.PROVIDER_CODE,
                        MARKET_DATA_SOURCE.PRIORITY,
                        MARKET_DATA_SOURCE.LIFECYCLE_STATUS
                )
                .from(MARKET_DATA_SOURCE)
                .where(condition)
                .orderBy(MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> toSource(
                        record.get(MARKET_DATA_SOURCE.PROVIDER_CODE),
                        record.get(MARKET_DATA_SOURCE.PRIORITY),
                        record.get(MARKET_DATA_SOURCE.LIFECYCLE_STATUS)
                ));

        if (sources.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new MarketDataSourcePlanQueryItem(
                captureProcessCode.value(),
                instrumentCode.value(),
                sources
        ));
    }

    @Override
    public List<MarketDataSourcePlanQueryItem> findAll() {
        // Таблица хранит строки источников, а API возвращает агрегированный план.
        Map<PlanKey, List<MarketDataSourceQueryItem>> groupedSources = new LinkedHashMap<>();

        dsl.select(
                        MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE,
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE,
                        MARKET_DATA_SOURCE.PROVIDER_CODE,
                        MARKET_DATA_SOURCE.PRIORITY,
                        MARKET_DATA_SOURCE.LIFECYCLE_STATUS
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
                            record.get(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE),
                            record.get(MARKET_DATA_SOURCE.INSTRUMENT_CODE)
                    );

                    MarketDataSourceQueryItem source = toSource(
                            record.get(MARKET_DATA_SOURCE.PROVIDER_CODE),
                            record.get(MARKET_DATA_SOURCE.PRIORITY),
                            record.get(MARKET_DATA_SOURCE.LIFECYCLE_STATUS)
                    );

                    groupedSources.computeIfAbsent(planKey, ignored -> new ArrayList<>()).add(source);
                });

        List<MarketDataSourcePlanQueryItem> plans = new ArrayList<>(groupedSources.size());

        for (Map.Entry<PlanKey, List<MarketDataSourceQueryItem>> entry : groupedSources.entrySet()) {
            PlanKey planKey = entry.getKey();
            plans.add(new MarketDataSourcePlanQueryItem(
                    planKey.captureProcessCode(),
                    planKey.instrumentCode(),
                    entry.getValue()
            ));
        }

        return List.copyOf(plans);
    }

    private static MarketDataSourceQueryItem toSource(
            String providerCode,
            Integer priority,
            String lifecycleStatus
    ) {
        Objects.requireNonNull(priority, "priority must not be null");

        return new MarketDataSourceQueryItem(providerCode, priority, lifecycleStatus);
    }

    private record PlanKey(
            String captureProcessCode,
            String instrumentCode
    ) {
    }
}
