package com.alligator.market.domain.provider.model.policy;

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
    /* Минимально допустимый интервал (ограничение). */
    private static final Duration MIN_ALLOWED = Duration.ofSeconds(1);

    public ProviderPolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        // Минимальный интервал обновления/запроса котировок не может быть меньше MIN_ALLOWED
        if (minUpdateInterval.compareTo(MIN_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }

    /* Удобная фабрика, если значения приходят в секундах. */
    public static ProviderPolicy ofSeconds(long seconds) {
        return new ProviderPolicy(Duration.ofSeconds(seconds));
    }
}
