package com.alligator.market.backend.provider.passport.api.query.list.mapper;

import com.alligator.market.backend.provider.passport.api.query.list.dto.PassportListItemResponse;
import com.alligator.market.backend.provider.passport.application.query.list.model.ProviderPassportListItem;

import java.util.Objects;

/**
 * Mapper between provider passport list read model and API response.
 */
public final class PassportListItemResponseMapper {

    private PassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PassportListItemResponse toResponse(ProviderPassportListItem item) {
        Objects.requireNonNull(item, "item must not be null");

        return new PassportListItemResponse(
                item.providerCode(),
                item.displayName(),
                item.deliveryMode(),
                item.accessMethod(),
                item.bulkSubscription(),
                item.lifecycleStatus()
        );
    }
}
