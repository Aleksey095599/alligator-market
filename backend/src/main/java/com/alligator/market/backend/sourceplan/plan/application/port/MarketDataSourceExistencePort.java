package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

/**
 * Port for checking the existence of a registered market data source by code.
 */
public interface MarketDataSourceExistencePort {

    /**
     * Checks whether a registered market data source exists with the specified code.
     */
    boolean existsByCode(MarketDataSourceCode code);
}
