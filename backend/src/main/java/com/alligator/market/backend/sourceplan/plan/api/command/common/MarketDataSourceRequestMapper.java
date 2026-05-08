package com.alligator.market.backend.sourceplan.plan.api.command.common;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

public class MarketDataSourceRequestMapper {
    public SourcePlanEntry toDomain(MarketDataSourceRequest request) {
        return new SourcePlanEntry(
                new MarketDataSourceCode(request.sourceCode()),
                request.priority()
        );
    }
}
