package com.alligator.market.backend.sourcing.plan.api.query.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourceResponse;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;

import java.util.List;

/**
 * Маппер доменной модели {@link MarketDataSourcePlan} в DTO read-side ответов.
 */
public class MarketDataSourcePlanResponseMapper {

    private final MarketDataSourceResponseMapper marketDataSourceResponseMapper;

    public MarketDataSourcePlanResponseMapper(MarketDataSourceResponseMapper marketDataSourceResponseMapper) {
        this.marketDataSourceResponseMapper = marketDataSourceResponseMapper;
    }

    /**
     * Конвертирует доменный {@link MarketDataSourcePlan} в DTO ответа {@link MarketDataSourcePlanResponse}.
     */
    public MarketDataSourcePlanResponse toPlanResponse(MarketDataSourcePlan plan) {
        List<MarketDataSourceResponse> sources = plan.sources().stream()
                .map(marketDataSourceResponseMapper::toSourceResponse)
                .toList();

        return new MarketDataSourcePlanResponse(
                plan.collectionProcessCode().value(),
                plan.instrumentCode().value(),
                sources
        );
    }
}
