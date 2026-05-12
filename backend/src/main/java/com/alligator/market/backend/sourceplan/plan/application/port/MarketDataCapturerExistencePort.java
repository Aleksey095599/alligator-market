package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.capturer.vo.CapturerCode;

public interface MarketDataCapturerExistencePort {
    boolean existsByCode(CapturerCode code);
}
