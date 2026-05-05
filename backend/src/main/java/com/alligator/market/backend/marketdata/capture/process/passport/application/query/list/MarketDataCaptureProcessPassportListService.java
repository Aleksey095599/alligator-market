package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.model.MarketDataCaptureProcessPassportListItem;
import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.port.MarketDataCaptureProcessPassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Use-case service for reading market data capture process passport projection rows.
 */
@Slf4j
public final class MarketDataCaptureProcessPassportListService {

    private final MarketDataCaptureProcessPassportListQueryPort queryPort;

    public MarketDataCaptureProcessPassportListService(
            MarketDataCaptureProcessPassportListQueryPort queryPort
    ) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    /**
     * Returns all capture process passport projection rows.
     */
    public List<MarketDataCaptureProcessPassportListItem> findAll() {
        List<MarketDataCaptureProcessPassportListItem> passports = queryPort.findAll();
        log.debug("Found {} capture process passport projection rows", passports.size());
        return passports;
    }
}
