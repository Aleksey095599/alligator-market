package com.alligator.market.domain.process.quotemonitor.capturer;

import com.alligator.market.domain.capturer.policy.CapturerPolicy;

import java.time.Duration;
import java.util.Objects;

public record LiveQuoteMonitorCapturerPolicy(
        Duration captureInterval
) implements CapturerPolicy {
    private static final Duration MIN_CAPTURE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public LiveQuoteMonitorCapturerPolicy {
        Objects.requireNonNull(captureInterval, "captureInterval must not be null");

        if (captureInterval.compareTo(MIN_CAPTURE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("captureInterval must be >= PT1S");
        }
    }
}
