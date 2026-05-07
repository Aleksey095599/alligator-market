package com.alligator.market.backend.sourceplan.plan.api.query.list.dto;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.MarketDataSourcePlanResponse;

import java.util.List;

/**
 * DTO ответа со списком планов источников по инструментам.
 */
public record MarketDataSourcePlanListResponse(
        List<MarketDataSourcePlanResponse> plans
) {
}
