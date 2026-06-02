package com.alligator.market.backend.sourceplan.plan.api.query.common.mapper;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourcePlanResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourceResponse;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;

import java.util.List;

public class SourcePlanResponseMapper {
    private final SourceResponseMapper sourceResponseMapper;

    public SourcePlanResponseMapper(SourceResponseMapper sourceResponseMapper) {
        this.sourceResponseMapper = sourceResponseMapper;
    }

    public SourcePlanResponse toPlanResponse(SourcePlanQueryItem plan) {
        List<SourceResponse> sources = plan.sources().stream()
                .map(sourceResponseMapper::toSourceResponse)
                .toList();

        return new SourcePlanResponse(
                plan.capturerCode(),
                plan.capturerRegistryStatus().name(),
                plan.planExecutionStatus().name(),
                plan.instrumentCode(),
                sources
        );
    }
}
