package com.alligator.market.backend.sourceplan.plan.application.exception;

import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Objects;

public final class CapturerCodeNotFoundException extends IllegalArgumentException {
    public CapturerCodeNotFoundException(CapturerCode capturerCode) {
        super("Capturer code '" + Objects.requireNonNull(capturerCode,
                "capturerCode must not be null").value() + "' does not exist");
    }
}
