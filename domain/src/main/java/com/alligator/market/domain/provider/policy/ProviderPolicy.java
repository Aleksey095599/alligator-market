package com.alligator.market.domain.provider.policy;

import java.time.Duration;
import java.util.Objects;

/**
 * Политика провайдера: иммутабельные параметры, которые использует бизнес-логика.
 *
 * @param minUpdateInterval Минимальный интервал обновления/запроса котировок
 * @param maxBatchSize      Максимальный размер батча на один запрос (параметр для дальнейшего масштабирования)
 */
public record ProviderPolicy(
        Duration minUpdateInterval,
        int maxBatchSize
) {
    /* Минимально допустимый интервал (ограничение). */
    private static final Duration MIN_ALLOWED = Duration.ofSeconds(1);

    /* Минимально допустимый размер батча (ограничение). */
    private static final int MIN_BATCH_SIZE = 1;

    public ProviderPolicy {
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        // Минимальный интервал обновления/запроса котировок не может быть меньше MIN_ALLOWED
        if (minUpdateInterval.compareTo(MIN_ALLOWED) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be >= PT1S");
        }

        // Размер батча должен быть положительным
        if (maxBatchSize < MIN_BATCH_SIZE) {
            throw new IllegalArgumentException("maxBatchSize must be >= 1");
        }
    }
}
