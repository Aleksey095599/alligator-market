package com.alligator.market.domain.provider.contract.policy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика провайдера: иммутабельные параметры, которые использует бизнес-логика.
 *
 * @param minUpdateInterval Минимальный интервал обновления котировок (независимо от режима доставки)
 */
public record ProviderPolicy(
        Duration minUpdateInterval
) {
    /* Минимально допустимый интервал. */
    private static final Duration MIN_ALLOWED = Duration.ofSeconds(1);

    public ProviderPolicy {
        // ↓↓ Базовая валидация аргументов
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");
        if (minUpdateInterval.compareTo(MIN_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }
    }

    /* Удобная фабрика, если значения приходят в секундах. */
    public static ProviderPolicy ofSeconds(long seconds) {
        return new ProviderPolicy(Duration.ofSeconds(seconds));
    }
}
