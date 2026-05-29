package com.alligator.market.backend.sourceplan.plan.application.query.options.model;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

import java.util.Objects;

public record CapturerOption(
        CapturerCode code,
        CapturerDisplayName displayName
) {
    public CapturerOption {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
