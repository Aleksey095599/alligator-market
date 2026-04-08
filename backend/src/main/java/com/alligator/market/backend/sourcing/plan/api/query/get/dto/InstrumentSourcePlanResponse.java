package com.alligator.market.backend.sourcing.plan.api.query.get.dto;

import java.util.List;

/**
 * DTO ответа с планом источников по инструменту.
 */
public record InstrumentSourcePlanResponse(
        String instrumentCode,
        List<MarketDataSourceResponse> sources
) {
}
