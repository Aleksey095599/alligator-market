package com.alligator.market.backend.sourcing.plan.api.query.common;

import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.springframework.stereotype.Component;

/**
 * Маппер доменной модели {@link MarketDataSource} в DTO read-side ответов.
 */
@Component
public class MarketDataSourceResponseMapper {

    /**
     * Конвертирует доменный {@link MarketDataSource} в DTO ответа {@link MarketDataSourceResponse}.
     */
    public MarketDataSourceResponse toSourceResponse(MarketDataSource source) {
        return new MarketDataSourceResponse(
                source.providerCode().value(),
                source.active(),
                source.priority()
        );
    }
}
