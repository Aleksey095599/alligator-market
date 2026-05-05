package com.alligator.market.backend.sourcing.plan.api.query.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourceResponse;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

/**
 * Маппер доменной модели {@link MarketDataSource} в DTO read-side ответов.
 */
public class MarketDataSourceResponseMapper {

    /**
     * Конвертирует доменный {@link MarketDataSource} в DTO ответа {@link MarketDataSourceResponse}.
     */
    public MarketDataSourceResponse toSourceResponse(MarketDataSource source) {
        return new MarketDataSourceResponse(
                source.providerCode().value(),
                source.priority()
        );
    }
}
