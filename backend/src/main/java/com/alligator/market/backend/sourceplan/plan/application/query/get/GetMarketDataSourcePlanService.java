package com.alligator.market.backend.sourceplan.plan.application.query.get;

import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.MarketDataSourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

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
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return marketDataSourcePlanQueryPort
                .findByMarketDataCapturerCodeAndInstrumentCode(capturerCode, instrumentCode)
                .orElseThrow(() -> new MarketDataSourcePlanNotFoundException(capturerCode, instrumentCode));
    }
}
