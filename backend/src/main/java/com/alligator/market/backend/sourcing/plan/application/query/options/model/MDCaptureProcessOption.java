package com.alligator.market.backend.sourcing.plan.application.query.options.model;

import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessDisplayName;

import java.util.Objects;

/**
 * Option процесса захвата рыночных данных для UI.
 */
public record MDCaptureProcessOption(
        MDCaptureProcessCode code,
        MDCaptureProcessDisplayName displayName
) {

    public MDCaptureProcessOption {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
