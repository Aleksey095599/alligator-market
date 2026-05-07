package com.alligator.market.backend.sourcing.plan.api.command.common;

import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;

/**
 * Маппер для {@link MarketDataSourceRequest}.
 */
public class MarketDataSourceRequestMapper {

    /**
     * Преобразует {@link MarketDataSourceRequest} в доменную модель {@link MarketDataSourcePlanEntry}.
     */
    public MarketDataSourcePlanEntry toDomain(MarketDataSourceRequest request) {
        return new MarketDataSourcePlanEntry(
                new ProviderCode(request.providerCode()),
                request.priority()
        );
    }
}
