package com.alligator.market.domain.capturer.passport;

import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

import java.util.Objects;

public record CapturerPassport(
        CapturerDisplayName displayName
) {

    public CapturerPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
