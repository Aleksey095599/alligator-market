package com.alligator.market.backend.sourcing.plan.api.query.get.dto;

/**
 * DTO источника рыночных данных в ответе плана инструмента.
 */
public record MarketDataSourceResponse(
        String providerCode,
        boolean active,
        int priority
) {
}
