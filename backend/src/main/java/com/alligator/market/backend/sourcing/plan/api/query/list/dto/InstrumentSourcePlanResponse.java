package com.alligator.market.backend.sourcing.plan.api.query.list.dto;

import java.util.List;

/**
 * DTO плана источников рыночных данных в ответе API.
 */
public record InstrumentSourcePlanResponse(
        String instrumentCode,
        List<InstrumentMarketDataSourceResponse> sources
) {
}
