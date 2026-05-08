package com.alligator.market.backend.capturer.passport.application.query.list.model;

import java.util.Objects;

/**
 * Read model for one market data capturer passport list item.
 */
public record MarketDataCapturerPassportListItem(
        String capturerCode,
        String displayName,
        String lifecycleStatus
) {

    public MarketDataCapturerPassportListItem {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
