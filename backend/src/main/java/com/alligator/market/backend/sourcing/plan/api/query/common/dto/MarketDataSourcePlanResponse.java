package com.alligator.market.backend.sourcing.plan.api.query.common.dto;

import java.util.List;

/**
 * DTO ответа с планом источников для инструмента.
 */
public record MarketDataSourcePlanResponse(
        String instrumentCode,
        List<MarketDataSourceResponse> sources
) {
}
