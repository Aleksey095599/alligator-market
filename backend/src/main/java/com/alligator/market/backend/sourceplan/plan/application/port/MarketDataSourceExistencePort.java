package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

public interface MarketDataSourceExistencePort {
    boolean existsByCode(MarketDataSourceCode code);
}
