package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.List;

/**
 * Port for loading available market data source codes for the UI.
 */
public interface MarketDataSourceOptionsQueryPort {

    /**
     * Returns all available market data source codes.
     */
    List<MarketDataSourceCode> findAllSourceCodes();
}
