package com.alligator.market.domain.source.policy;

import java.time.Duration;
import java.util.Objects;

/**
 * Runtime policy of a market data source.
 *
 * @param minUpdateInterval the minimum interval between market data update requests
 */
public record MarketDataSourcePolicy(
        Duration minUpdateInterval
) {
    /* Lower bound for {@code minUpdateInterval}. */
    private static final Duration MIN_UPDATE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public MarketDataSourcePolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        if (minUpdateInterval.compareTo(MIN_UPDATE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }
}
