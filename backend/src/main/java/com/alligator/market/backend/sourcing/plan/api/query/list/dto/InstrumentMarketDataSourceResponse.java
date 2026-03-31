package com.alligator.market.backend.sourcing.plan.api.query.list.dto;

/**
 * DTO источника рыночных данных в ответе API.
 */
public record InstrumentMarketDataSourceResponse(
        String providerCode,
        boolean active,
        int priority
) {
}
