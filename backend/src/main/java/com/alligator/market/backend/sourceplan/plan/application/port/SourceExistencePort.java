package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.source.vo.SourceCode;

public interface SourceExistencePort {
    boolean existsByCode(SourceCode code);
}
