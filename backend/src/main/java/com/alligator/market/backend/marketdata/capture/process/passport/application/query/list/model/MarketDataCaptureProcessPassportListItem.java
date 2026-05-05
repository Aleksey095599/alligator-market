package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.model;

import java.util.Objects;

/**
 * Read model for one market data capture process passport list item.
 */
public record MarketDataCaptureProcessPassportListItem(
        String captureProcessCode,
        String displayName,
        String lifecycleStatus
) {

    public MarketDataCaptureProcessPassportListItem {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
