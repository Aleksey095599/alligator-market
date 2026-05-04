package com.alligator.market.domain.marketdata.collection.process.policy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика процесса сбора рыночных данных: иммутабельные параметры для бизнес-логики.
 *
 * @param captureInterval интервал между запросами котировок в рамках процесса сбора
 */
public record CollectionProcessPolicy(
        Duration captureInterval
) {

    private static final Duration MIN_CAPTURE_INTERVAL = Duration.ofSeconds(1);

    public CollectionProcessPolicy {
        Objects.requireNonNull(captureInterval, "captureInterval must not be null");

        if (captureInterval.compareTo(MIN_CAPTURE_INTERVAL) < 0) {
            throw new IllegalArgumentException("captureInterval must be >= PT1S");
        }
    }
}
