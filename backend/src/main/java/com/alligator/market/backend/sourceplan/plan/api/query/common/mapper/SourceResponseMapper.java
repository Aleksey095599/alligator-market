package com.alligator.market.backend.sourceplan.plan.api.query.common.mapper;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourceResponse;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourceQueryItem;

public class SourceResponseMapper {
    public SourceResponse toSourceResponse(SourceQueryItem source) {
        return new SourceResponse(
                source.sourceCode(),
                source.priority(),
                source.lifecycleStatus()
        );
    }
}
