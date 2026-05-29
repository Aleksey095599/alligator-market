package com.alligator.market.backend.capturer.passport.application.query.list.model;

import java.util.Objects;

public record CapturerPassportListItem(
        String capturerCode,
        String displayName,
        String lifecycleStatus
) {
    public CapturerPassportListItem {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
