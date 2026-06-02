package com.alligator.market.backend.capturer.passport.api.query.list.mapper;

import com.alligator.market.backend.capturer.passport.api.query.list.dto.CapturerPassportListItemResponse;
import com.alligator.market.backend.capturer.passport.application.query.list.model.CapturerPassportListItem;

import java.util.Objects;

public final class CapturerPassportListItemResponseMapper {
    private CapturerPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CapturerPassportListItemResponse toResponse(
            CapturerPassportListItem item
    ) {
        Objects.requireNonNull(item, "item must not be null");

        return new CapturerPassportListItemResponse(
                item.capturerCode(),
                item.displayName(),
                item.registryStatus().name()
        );
    }
}
