package com.alligator.market.domain.source.policy;

import java.time.Duration;
import java.util.Objects;

public record SourcePolicy(
        Duration minUpdateInterval
) {
    private static final Duration MIN_UPDATE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public SourcePolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        if (minUpdateInterval.compareTo(MIN_UPDATE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }
}
