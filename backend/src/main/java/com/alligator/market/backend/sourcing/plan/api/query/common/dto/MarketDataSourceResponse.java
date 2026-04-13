package com.alligator.market.backend.sourcing.plan.api.query.common.dto;

/**
 * DTO источника рыночных данных для ответов.
 */
public record MarketDataSourceResponse(
        String providerCode,
        boolean active,
        int priority
) {
}
