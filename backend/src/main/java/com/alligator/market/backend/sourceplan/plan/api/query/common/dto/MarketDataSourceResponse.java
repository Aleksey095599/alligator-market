package com.alligator.market.backend.sourceplan.plan.api.query.common.dto;

public record MarketDataSourceResponse(
        String sourceCode,
        int priority,
        String lifecycleStatus
) {
}
