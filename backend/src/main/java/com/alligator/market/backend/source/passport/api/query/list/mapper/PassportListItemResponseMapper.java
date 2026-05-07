package com.alligator.market.backend.source.passport.api.query.list.mapper;

import com.alligator.market.backend.source.passport.api.query.list.dto.PassportListItemResponse;
import com.alligator.market.backend.source.passport.application.query.list.model.MarketDataSourcePassportListItem;

import java.util.Objects;

/**
 * Mapper between source passport list read model and API response.
 */
public final class PassportListItemResponseMapper {

    private PassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PassportListItemResponse toResponse(MarketDataSourcePassportListItem item) {
        Objects.requireNonNull(item, "item must not be null");

        return new PassportListItemResponse(
                item.sourceCode(),
                item.displayName(),
                item.deliveryMode(),
                item.accessMethod(),
                item.bulkSubscription(),
                item.lifecycleStatus()
        );
    }
}
