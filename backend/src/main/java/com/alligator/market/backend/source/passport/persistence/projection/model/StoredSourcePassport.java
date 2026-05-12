package com.alligator.market.backend.source.passport.persistence.projection.model;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record StoredSourcePassport(
        SourceCode sourceCode,
        SourcePassport passport,
        StoredSourceProjectionLifecycleStatus lifecycleStatus
) {
    public StoredSourcePassport {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
