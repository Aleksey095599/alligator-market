package com.alligator.market.domain.provider.contract.settings;

/**
 * Иммутабельные настройки провайдера.
 *
 * @param minPoll
 */
public record ProviderSettings(
        java.time.Duration minPoll
) {
    // ↓↓ Базовые проверки аргументов
    public ProviderSettings {
        java.util.Objects.requireNonNull(minPoll, "minPoll must not be null");
        if (minPoll.isZero() || minPoll.isNegative()) {
            throw new IllegalArgumentException("minPoll must be positive");
        }
    }
}
