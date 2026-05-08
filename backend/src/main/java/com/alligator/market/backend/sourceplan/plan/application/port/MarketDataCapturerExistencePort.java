package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

public interface MarketDataCapturerExistencePort {
    boolean existsByCode(MarketDataCapturerCode code);
}
