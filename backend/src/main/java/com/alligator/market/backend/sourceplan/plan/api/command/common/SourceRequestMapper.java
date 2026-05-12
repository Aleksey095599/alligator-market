package com.alligator.market.backend.sourceplan.plan.api.command.common;

import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

public class SourceRequestMapper {
    public SourcePlanEntry toDomain(SourceRequest request) {
        return new SourcePlanEntry(
                new SourceCode(request.sourceCode()),
                request.priority()
        );
    }
}
