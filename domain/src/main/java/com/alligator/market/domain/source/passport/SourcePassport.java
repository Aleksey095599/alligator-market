package com.alligator.market.domain.source.passport;

import com.alligator.market.domain.source.passport.vo.SourceDisplayName;

import java.util.Objects;

public record SourcePassport(
        SourceDisplayName displayName
) {

    public SourcePassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
