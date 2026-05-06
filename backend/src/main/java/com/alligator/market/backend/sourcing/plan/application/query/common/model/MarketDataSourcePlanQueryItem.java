package com.alligator.market.backend.sourcing.plan.application.query.common.model;

import java.util.List;
import java.util.Objects;

public record MarketDataSourcePlanQueryItem(
        String captureProcessCode,
        String instrumentCode,
        List<MarketDataSourceQueryItem> sources
) {

    public MarketDataSourcePlanQueryItem {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");
        sources = List.copyOf(sources);
    }
}
