package com.alligator.market.backend.sourceplan.plan.api.query.common.dto;

import java.util.List;

public record SourcePlanResponse(
        String capturerCode,
        String capturerLifecycleStatus,
        String planExecutionStatus,
        String instrumentCode,
        List<MarketDataSourceResponse> sources
) {
}
