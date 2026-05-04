package com.alligator.market.domain.marketdata.collection.process.catalog.twap.analytical.lastprice;

import com.alligator.market.domain.marketdata.collection.process.policy.CollectionProcessPolicy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика аналитического TWAP-процесса на основе тиков последней цены.
 *
 * @param captureInterval фактический интервал фиксации тиков
 */
public record AnalyticalTwapLastPriceCollectionProcessPolicy(
        Duration captureInterval
) implements CollectionProcessPolicy {

    public AnalyticalTwapLastPriceCollectionProcessPolicy {
        Objects.requireNonNull(captureInterval, "captureInterval must not be null");

        if (captureInterval.isZero() || captureInterval.isNegative()) {
            throw new IllegalArgumentException("captureInterval must be positive");
        }
    }
}
