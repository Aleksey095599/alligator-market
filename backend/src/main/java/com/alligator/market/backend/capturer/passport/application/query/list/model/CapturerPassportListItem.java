package com.alligator.market.backend.capturer.passport.application.query.list.model;

import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;

import java.util.Objects;

public record CapturerPassportListItem(
        String capturerCode,
        String displayName,
        String description,
        CapturerPassportRecord.RegistryStatus registryStatus
) {
    public CapturerPassportListItem {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }
}
