package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.properties;

import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandlerPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Objects;

@Validated
@ConfigurationProperties("market-data-source.handler-properties.moex.iss.fx-spot")
public record MoexIssFxSpotHandlerProperties(
        Duration pollInterval
) {

    public MoexIssFxSpotHandlerProperties {
        Objects.requireNonNull(pollInterval, "pollInterval must not be null");

        if (pollInterval.compareTo(MoexIssFxSpotHandlerPolicy.MIN_POLL_INTERVAL) < 0) {
            throw new IllegalArgumentException(
                    "pollInterval must be >= " + MoexIssFxSpotHandlerPolicy.MIN_POLL_INTERVAL
            );
        }
    }
}
