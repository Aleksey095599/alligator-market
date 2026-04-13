package com.alligator.market.backend.sourcing.plan.api.query.common;

/**
 * DTO источника рыночных данных для HTTP-ответа.
 */
public record MarketDataSourceResponse(
        String providerCode,
        boolean active,
        int priority
) {
}
