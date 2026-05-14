package com.alligator.market.backend.source.passport.persistence.projection.model;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistryStatus;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record StoredSourcePassport(
        SourceCode sourceCode,
        SourcePassport passport,
        StoredSourcePassportRegistryStatus registryStatus
) {
    public StoredSourcePassport {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }
}
