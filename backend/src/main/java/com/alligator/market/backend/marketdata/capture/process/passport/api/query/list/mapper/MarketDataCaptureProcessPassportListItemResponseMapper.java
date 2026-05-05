package com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.mapper;

import com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.dto.MarketDataCaptureProcessPassportListItemResponse;
import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.model.MarketDataCaptureProcessPassportListItem;

import java.util.Objects;

/**
 * Mapper between capture process passport list read model and API response.
 */
public final class MarketDataCaptureProcessPassportListItemResponseMapper {

    private MarketDataCaptureProcessPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MarketDataCaptureProcessPassportListItemResponse toResponse(
            MarketDataCaptureProcessPassportListItem item
    ) {
        Objects.requireNonNull(item, "item must not be null");

        return new MarketDataCaptureProcessPassportListItemResponse(
                item.captureProcessCode(),
                item.displayName(),
                item.lifecycleStatus()
        );
    }
}
