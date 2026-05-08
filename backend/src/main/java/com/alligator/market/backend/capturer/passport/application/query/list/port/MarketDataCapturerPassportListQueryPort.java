package com.alligator.market.backend.capturer.passport.application.query.list.port;

import com.alligator.market.backend.capturer.passport.application.query.list.model.MarketDataCapturerPassportListItem;

import java.util.List;

/**
 * Port for reading market data capturer passport projection rows.
 */
public interface MarketDataCapturerPassportListQueryPort {

    /**
     * Returns all capturer passport projection rows, including retired rows.
     */
    List<MarketDataCapturerPassportListItem> findAll();
}
