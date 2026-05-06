package com.alligator.market.backend.sourcing.plan.application.query.get;

import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.backend.sourcing.plan.application.query.common.port.MarketDataSourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.Objects;

public final class GetMarketDataSourcePlanService {

    private final MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort;

    public GetMarketDataSourcePlanService(MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort) {
        this.marketDataSourcePlanQueryPort = Objects.requireNonNull(
                marketDataSourcePlanQueryPort,
                "marketDataSourcePlanQueryPort must not be null"
        );
    }

    public MarketDataSourcePlanQueryItem get(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return marketDataSourcePlanQueryPort
                .findByMarketDataCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode)
                .orElseThrow(() -> new MarketDataSourcePlanNotFoundException(captureProcessCode, instrumentCode));
    }
}
