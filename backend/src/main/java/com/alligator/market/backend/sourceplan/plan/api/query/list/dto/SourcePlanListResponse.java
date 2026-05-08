package com.alligator.market.backend.sourceplan.plan.api.query.list.dto;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourcePlanResponse;

import java.util.List;

/**
 * DTO ответа со списком планов источников по инструментам.
 */
public record SourcePlanListResponse(
        List<SourcePlanResponse> plans
) {
}
