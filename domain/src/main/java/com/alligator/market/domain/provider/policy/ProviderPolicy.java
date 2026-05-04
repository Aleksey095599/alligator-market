package com.alligator.market.domain.provider.policy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика провайдера: иммутабельные параметры, которые использует бизнес-логика.
 *
 * @param minUpdateInterval Минимальный интервал обновления/запроса котировок для заданного провайдера
 */
public record ProviderPolicy(
        Duration minUpdateInterval
) {
    /* Ограничение снизу для {@code minUpdateInterval}. */
    private static final Duration MIN_UPDATE_INTERVAL_ALLOWED = Duration.ofSeconds(1);

    public ProviderPolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        if (minUpdateInterval.compareTo(MIN_UPDATE_INTERVAL_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }
}
