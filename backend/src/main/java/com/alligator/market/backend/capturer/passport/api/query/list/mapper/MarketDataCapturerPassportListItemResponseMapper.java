package com.alligator.market.backend.capturer.passport.api.query.list.mapper;

import com.alligator.market.backend.capturer.passport.api.query.list.dto.MarketDataCapturerPassportListItemResponse;
import com.alligator.market.backend.capturer.passport.application.query.list.model.MarketDataCapturerPassportListItem;

import java.util.Objects;

public final class MarketDataCapturerPassportListItemResponseMapper {
    private MarketDataCapturerPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MarketDataCapturerPassportListItemResponse toResponse(
            MarketDataCapturerPassportListItem item
    ) {
        Objects.requireNonNull(item, "item must not be null");

        return new MarketDataCapturerPassportListItemResponse(
                item.capturerCode(),
                item.displayName(),
                item.lifecycleStatus()
        );
    }
}
