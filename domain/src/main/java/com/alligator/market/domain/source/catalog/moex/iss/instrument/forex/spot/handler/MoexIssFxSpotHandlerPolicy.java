package com.alligator.market.domain.source.catalog.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;

import java.time.Duration;
import java.util.Objects;

public record MoexIssFxSpotHandlerPolicy(
        Duration pollInterval
) implements SourceHandlerPolicy {
    private static final Duration MIN_POLL_INTERVAL_ALLOWED = Duration.ofSeconds(1);
    public static final Duration POLL_INTERVAL = Duration.ofSeconds(5);
    public static final MoexIssFxSpotHandlerPolicy INSTANCE = new MoexIssFxSpotHandlerPolicy(POLL_INTERVAL);

    public MoexIssFxSpotHandlerPolicy {
        Objects.requireNonNull(pollInterval, "pollInterval must not be null");

        if (pollInterval.compareTo(MIN_POLL_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("pollInterval must be >= PT1S");
        }
    }
}
