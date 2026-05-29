package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.capturer.vo.CapturerCode;

public interface CapturerExistencePort {
    boolean existsByCode(CapturerCode code);
}
