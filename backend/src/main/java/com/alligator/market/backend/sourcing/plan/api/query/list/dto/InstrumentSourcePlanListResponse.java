package com.alligator.market.backend.sourcing.plan.api.query.list.dto;

import com.alligator.market.backend.sourcing.plan.api.query.common.InstrumentSourcePlanResponse;

import java.util.List;

/**
 * DTO ответа со списком планов источников по инструментам.
 */
public record InstrumentSourcePlanListResponse(
        List<InstrumentSourcePlanResponse> plans
) {
}
