package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;

import java.time.Duration;
import java.util.Objects;

public record MoexIssFxSpotHandlerPolicy(
        Duration minUpdateInterval
) implements SourceHandlerPolicy {
    private static final Duration MIN_UPDATE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public MoexIssFxSpotHandlerPolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        if (minUpdateInterval.compareTo(MIN_UPDATE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }
}
