package com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler;

import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;

import java.time.Duration;
import java.util.Objects;

public record TwelveDataFxSpotHandlerPolicy(
        Duration pollInterval
) implements SourceHandlerPolicy {
    public static final Duration MIN_POLL_INTERVAL = Duration.ofMinutes(5);

    public TwelveDataFxSpotHandlerPolicy {
        Objects.requireNonNull(pollInterval, "pollInterval must not be null");

        if (pollInterval.compareTo(MIN_POLL_INTERVAL) < 0) {
            throw new IllegalArgumentException("pollInterval must be >= " + MIN_POLL_INTERVAL);
        }
    }
}
