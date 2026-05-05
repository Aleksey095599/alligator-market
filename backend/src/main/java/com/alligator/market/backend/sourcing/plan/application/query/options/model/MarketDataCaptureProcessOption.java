package com.alligator.market.backend.sourcing.plan.application.query.options.model;

import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessDisplayName;

import java.util.Objects;

/**
 * Option процесса захвата рыночных данных для UI.
 */
public record MarketDataCaptureProcessOption(
        MarketDataCaptureProcessCode code,
        MarketDataCaptureProcessDisplayName displayName
) {

    public MarketDataCaptureProcessOption {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
