package com.alligator.market.backend.sourcing.plan.api.command.common;

import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

/**
 * Маппер для {@link MarketDataSourceRequest}.
 */
public class MarketDataSourceRequestMapper {

    /**
     * Преобразует {@link MarketDataSourceRequest} в доменную модель {@link MarketDataSource}.
     */
    public MarketDataSource toDomain(MarketDataSourceRequest request) {
        return new MarketDataSource(
                new ProviderCode(request.providerCode()),
                request.active(),
                request.priority()
        );
    }
}
