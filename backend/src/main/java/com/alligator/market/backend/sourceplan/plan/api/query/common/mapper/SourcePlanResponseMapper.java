package com.alligator.market.backend.sourceplan.plan.api.query.common.mapper;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourcePlanResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.MarketDataSourceResponse;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;

import java.util.List;

public class SourcePlanResponseMapper {
    private final MarketDataSourceResponseMapper marketDataSourceResponseMapper;

    public SourcePlanResponseMapper(MarketDataSourceResponseMapper marketDataSourceResponseMapper) {
        this.marketDataSourceResponseMapper = marketDataSourceResponseMapper;
    }

    public SourcePlanResponse toPlanResponse(SourcePlanQueryItem plan) {
        List<MarketDataSourceResponse> sources = plan.sources().stream()
                .map(marketDataSourceResponseMapper::toSourceResponse)
                .toList();

        return new SourcePlanResponse(
                plan.capturerCode(),
                plan.capturerLifecycleStatus(),
                plan.planExecutionStatus().name(),
                plan.instrumentCode(),
                sources
        );
    }
}
