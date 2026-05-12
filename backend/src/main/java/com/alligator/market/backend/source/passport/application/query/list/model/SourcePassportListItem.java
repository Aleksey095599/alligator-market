package com.alligator.market.backend.source.passport.application.query.list.model;

import java.util.Objects;

public record SourcePassportListItem(
        String sourceCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        String lifecycleStatus
) {
    public SourcePassportListItem {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
