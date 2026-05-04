package com.alligator.market.domain.marketdata.capture.process.catalog.twap.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.policy.CaptureProcessPolicy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика аналитического TWAP-процесса на основе тиков последней цены.
 *
 * @param captureInterval фактический интервал фиксации тиков
 */
public record AnalyticalTwapByLastPricePolicy(
        Duration captureInterval
) implements CaptureProcessPolicy {

    public AnalyticalTwapByLastPricePolicy {
        Objects.requireNonNull(captureInterval, "captureInterval must not be null");

        if (captureInterval.isZero() || captureInterval.isNegative()) {
            throw new IllegalArgumentException("captureInterval must be positive");
        }
    }
}
