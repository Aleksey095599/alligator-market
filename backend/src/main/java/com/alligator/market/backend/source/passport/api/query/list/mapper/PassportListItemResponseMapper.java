package com.alligator.market.backend.source.passport.api.query.list.mapper;

import com.alligator.market.backend.source.passport.api.query.list.dto.PassportListItemResponse;
import com.alligator.market.backend.source.passport.application.query.list.model.SourcePassportListItem;

import java.util.Objects;

public final class PassportListItemResponseMapper {
    private PassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PassportListItemResponse toResponse(SourcePassportListItem item) {
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
