package com.alligator.market.domain.source.catalog.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.domain.source.catalog.shared.MinPollAllowedInterval;
import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;

import java.time.Duration;

public record MoexIssFxSpotHandlerPolicy(
        Duration pollInterval
) implements SourceHandlerPolicy {
    public static final Duration POLL_INTERVAL = Duration.ofSeconds(5);
    public static final MoexIssFxSpotHandlerPolicy INSTANCE = new MoexIssFxSpotHandlerPolicy(POLL_INTERVAL);

    public MoexIssFxSpotHandlerPolicy {
        MinPollAllowedInterval.requireSatisfiedBy(pollInterval);
    }
}
