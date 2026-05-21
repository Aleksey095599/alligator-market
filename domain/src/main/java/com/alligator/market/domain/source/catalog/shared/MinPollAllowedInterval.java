package com.alligator.market.domain.source.catalog.shared;

import java.time.Duration;
import java.util.Objects;

public final class MinPollAllowedInterval {
    public static final Duration VALUE = Duration.ofSeconds(1);

    private MinPollAllowedInterval() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void requireSatisfiedBy(Duration pollInterval) {
        Objects.requireNonNull(pollInterval, "pollInterval must not be null");

        if (pollInterval.compareTo(VALUE) < 0) {
            throw new IllegalArgumentException("pollInterval must be >= " + VALUE);
        }
    }
}
