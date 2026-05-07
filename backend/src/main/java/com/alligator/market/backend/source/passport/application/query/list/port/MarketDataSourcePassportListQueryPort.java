package com.alligator.market.backend.source.passport.application.query.list.port;

import com.alligator.market.backend.source.passport.application.query.list.model.MarketDataSourcePassportListItem;

import java.util.List;

/**
 * Port for reading source passport projection rows.
 */
public interface MarketDataSourcePassportListQueryPort {

    /**
     * Returns all source passport projection rows, including retired rows.
     */
    List<MarketDataSourcePassportListItem> findAll();
}
