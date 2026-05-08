package com.alligator.market.backend.sourceplan.plan.api.query.common.dto;

/**
 * API response item for one source plan entry.
 */
public record MarketDataSourceResponse(
        String sourceCode,
        int priority,
        String lifecycleStatus
) {
}
