package com.alligator.market.backend.sourceplan.plan.api.command.common;

import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.PrioritizedSourceCode;

public class SourceRequestMapper {
    public PrioritizedSourceCode toDomain(SourceRequest request) {
        return new PrioritizedSourceCode(
                new SourceCode(request.sourceCode()),
                request.priority()
        );
    }
}
