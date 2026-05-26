package com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.properties;

import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandlerPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Objects;

@Validated
@ConfigurationProperties("market-source.handler-properties.twelve-data.fx-spot")
public record TwelveDataFxSpotHandlerProperties(
        Duration pollInterval
) {

    public TwelveDataFxSpotHandlerProperties {
        Objects.requireNonNull(pollInterval, "pollInterval must not be null");

        if (pollInterval.compareTo(TwelveDataFxSpotHandlerPolicy.MIN_POLL_INTERVAL) < 0) {
            throw new IllegalArgumentException(
                    "pollInterval must be >= " + TwelveDataFxSpotHandlerPolicy.MIN_POLL_INTERVAL
            );
        }
    }
}
