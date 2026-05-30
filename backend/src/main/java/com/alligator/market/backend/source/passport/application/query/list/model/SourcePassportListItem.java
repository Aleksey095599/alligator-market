package com.alligator.market.backend.source.passport.application.query.list.model;

import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport;

import java.util.Objects;

public record SourcePassportListItem(
        String sourceCode,
        String displayName,
        StoredSourcePassport.Status lifecycleStatus
) {
    public SourcePassportListItem {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
