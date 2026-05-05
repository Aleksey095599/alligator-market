package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.port;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.model.MarketDataCaptureProcessPassportListItem;

import java.util.List;

/**
 * Port for reading market data capture process passport projection rows.
 */
public interface MarketDataCaptureProcessPassportListQueryPort {

    /**
     * Returns all capture process passport projection rows, including retired rows.
     */
    List<MarketDataCaptureProcessPassportListItem> findAll();
}
