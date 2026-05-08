package com.alligator.market.backend.sourceplan.plan.api.query.list.dto;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourcePlanResponse;

import java.util.List;

public record SourcePlanListResponse(
        List<SourcePlanResponse> plans
) {
}
