package com.alligator.market.backend.sourceplan.plan.api.command.common;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;

/**
 * Mapper for {@link MarketDataSourceRequest}.
 */
public class MarketDataSourceRequestMapper {

    /**
     * Converts {@link MarketDataSourceRequest} to {@link MarketDataSourcePlanEntry}.
     */
    public MarketDataSourcePlanEntry toDomain(MarketDataSourceRequest request) {
        return new MarketDataSourcePlanEntry(
                new MarketDataSourceCode(request.sourceCode()),
                request.priority()
        );
    }
}
