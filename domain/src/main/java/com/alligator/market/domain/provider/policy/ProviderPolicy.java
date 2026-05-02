package com.alligator.market.domain.provider.policy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика провайдера: иммутабельные параметры, которые использует бизнес-логика.
 *
 * @param minUpdateInterval Минимальный интервал обновления/запроса котировок
 */
public record ProviderPolicy(
        Duration minUpdateInterval
) {
    /* Ограничение для минимального интервала обновления/запроса котировок. */
    private static final Duration MIN_UPDATE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public ProviderPolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        if (minUpdateInterval.compareTo(MIN_UPDATE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }
}
