package com.alligator.market.backend.sourcing.plan.application.query.get;

import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;

import java.util.Objects;

/**
 * Сервис чтения плана источников рыночных данных для инструмента.
 */
public final class GetMarketDataSourcePlanService {

    /* Репозиторий планов источников. */
    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    public GetMarketDataSourcePlanService(MarketDataSourcePlanRepository marketDataSourcePlanRepository) {
        this.marketDataSourcePlanRepository = Objects.requireNonNull(
                marketDataSourcePlanRepository,
                "marketDataSourcePlanRepository must not be null"
        );
    }

    /**
     * Возвращает план источников для инструмента.
     */
    public MarketDataSourcePlan get(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return marketDataSourcePlanRepository
                .findByCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode)
                .orElseThrow(() -> new MarketDataSourcePlanNotFoundException(captureProcessCode, instrumentCode));
    }
}
