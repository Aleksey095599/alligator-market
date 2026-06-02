package com.alligator.market.backend.source.passport.application.query.list.model;

import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport;

import java.util.Objects;

public record SourcePassportListItem(
        String sourceCode,
        String displayName,
        StoredSourcePassport.RegistryStatus registryStatus
) {
    public SourcePassportListItem {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }
}
