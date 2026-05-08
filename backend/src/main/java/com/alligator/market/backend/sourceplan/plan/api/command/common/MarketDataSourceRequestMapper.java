package com.alligator.market.backend.sourceplan.plan.api.command.common;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

/**
 * Mapper for {@link MarketDataSourceRequest}.
 */
public class MarketDataSourceRequestMapper {

    /**
     * Converts {@link MarketDataSourceRequest} to {@link SourcePlanEntry}.
     */
    public SourcePlanEntry toDomain(MarketDataSourceRequest request) {
        return new SourcePlanEntry(
                new MarketDataSourceCode(request.sourceCode()),
                request.priority()
        );
    }
}
