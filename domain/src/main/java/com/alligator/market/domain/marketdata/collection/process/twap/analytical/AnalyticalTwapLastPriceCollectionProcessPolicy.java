package com.alligator.market.domain.marketdata.collection.process.twap.analytical;

import com.alligator.market.domain.marketdata.collection.process.policy.CollectionProcessPolicy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика аналитического TWAP-процесса на основе тиков последней цены.
 *
 * @param minCaptureInterval минимально допустимый интервал между фиксациями тиков
 */
public record AnalyticalTwapLastPriceCollectionProcessPolicy(
        Duration minCaptureInterval
) implements CollectionProcessPolicy {

    private static final Duration MIN_CAPTURE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public AnalyticalTwapLastPriceCollectionProcessPolicy {
        Objects.requireNonNull(minCaptureInterval, "minCaptureInterval must not be null");

        if (minCaptureInterval.compareTo(MIN_CAPTURE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("minCaptureInterval must be >= PT1S");
        }
    }
}
