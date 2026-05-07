package com.alligator.market.backend.sourceplan.plan.api.query.common.dto;

import java.util.List;

/**
 * DTO ответа с планом источников для инструмента.
 */
public record MarketDataSourcePlanResponse(
        String captureProcessCode,
        String instrumentCode,
        List<MarketDataSourceResponse> sources
) {
}
