package com.alligator.market.backend.sourceplan.plan.api.command.common;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;
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
                new MarketDataSourceCode(request.providerCode()),
                request.priority()
        );
    }
}
