package com.alligator.market.backend.sourceplan.plan.api.query.common.dto;

public record SourceResponse(
        String sourceCode,
        int priority,
        String lifecycleStatus
) {
}
