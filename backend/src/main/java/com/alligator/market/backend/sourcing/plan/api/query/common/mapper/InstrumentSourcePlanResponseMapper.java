package com.alligator.market.backend.sourcing.plan.api.query.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourceResponse;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;

import java.util.List;

/**
 * Маппер доменной модели {@link InstrumentSourcePlan} в DTO read-side ответов.
 */
public class InstrumentSourcePlanResponseMapper {

    private final MarketDataSourceResponseMapper marketDataSourceResponseMapper;

    public InstrumentSourcePlanResponseMapper(MarketDataSourceResponseMapper marketDataSourceResponseMapper) {
        this.marketDataSourceResponseMapper = marketDataSourceResponseMapper;
    }

    /**
     * Конвертирует доменный {@link InstrumentSourcePlan} в DTO ответа {@link InstrumentSourcePlanResponse}.
     */
    public InstrumentSourcePlanResponse toPlanResponse(InstrumentSourcePlan plan) {
        List<MarketDataSourceResponse> sources = plan.sources().stream()
                .map(marketDataSourceResponseMapper::toSourceResponse)
                .toList();

        return new InstrumentSourcePlanResponse(plan.instrumentCode().value(), sources);
    }
}
