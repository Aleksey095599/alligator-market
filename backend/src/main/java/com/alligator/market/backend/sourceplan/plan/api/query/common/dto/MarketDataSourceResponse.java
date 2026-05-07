package com.alligator.market.backend.sourceplan.plan.api.query.common.dto;

/**
 * DTO источника рыночных данных для ответов.
 */
public record MarketDataSourceResponse(
        String providerCode,
        int priority,
        // Технический статус пригодности source: ACTIVE или RETIRED.
        String lifecycleStatus
) {
}
