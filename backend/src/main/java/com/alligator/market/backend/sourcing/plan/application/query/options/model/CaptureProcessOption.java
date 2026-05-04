package com.alligator.market.backend.sourcing.plan.application.query.options.model;

import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessDisplayName;

import java.util.Objects;

/**
 * Option процесса фиксации рыночных данных для UI.
 */
public record CaptureProcessOption(
        CaptureProcessCode code,
        CaptureProcessDisplayName displayName
) {

    public CaptureProcessOption {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
