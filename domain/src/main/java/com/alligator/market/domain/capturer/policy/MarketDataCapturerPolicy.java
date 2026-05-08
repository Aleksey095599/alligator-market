package com.alligator.market.domain.capturer.policy;

import java.time.Duration;
import java.util.Objects;

public record MarketDataCapturerPolicy(
        Duration captureInterval
) {
    private static final Duration CAPTURE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public MarketDataCapturerPolicy {
        Objects.requireNonNull(captureInterval, "captureInterval must not be null");

        if (captureInterval.compareTo(CAPTURE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("captureInterval must be >= PT1S");
        }
    }
}
