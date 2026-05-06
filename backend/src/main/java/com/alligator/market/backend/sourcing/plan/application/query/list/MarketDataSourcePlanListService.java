package com.alligator.market.backend.sourcing.plan.application.query.list;

import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.backend.sourcing.plan.application.query.common.port.MarketDataSourcePlanQueryPort;

import java.util.List;
import java.util.Objects;

public final class MarketDataSourcePlanListService {

    private final MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort;

    public MarketDataSourcePlanListService(MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort) {
        this.marketDataSourcePlanQueryPort = Objects.requireNonNull(
                marketDataSourcePlanQueryPort,
                "marketDataSourcePlanQueryPort must not be null"
        );
    }

    public List<MarketDataSourcePlanQueryItem> list() {
        return marketDataSourcePlanQueryPort.findAll();
    }
}
