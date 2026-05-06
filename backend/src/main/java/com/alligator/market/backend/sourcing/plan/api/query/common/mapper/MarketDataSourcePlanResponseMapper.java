package com.alligator.market.backend.sourcing.plan.api.query.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourceResponse;
import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourcePlanQueryItem;

import java.util.List;

public class MarketDataSourcePlanResponseMapper {

    private final MarketDataSourceResponseMapper marketDataSourceResponseMapper;

    public MarketDataSourcePlanResponseMapper(MarketDataSourceResponseMapper marketDataSourceResponseMapper) {
        this.marketDataSourceResponseMapper = marketDataSourceResponseMapper;
    }

    public MarketDataSourcePlanResponse toPlanResponse(MarketDataSourcePlanQueryItem plan) {
        List<MarketDataSourceResponse> sources = plan.sources().stream()
                .map(marketDataSourceResponseMapper::toSourceResponse)
                .toList();

        return new MarketDataSourcePlanResponse(
                plan.captureProcessCode(),
                plan.instrumentCode(),
                sources
        );
    }
}
