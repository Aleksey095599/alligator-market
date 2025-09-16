package com.alligator.market.domain.provider.contract.settings;

/**
 * Иммутабельные настройки провайдера.
 *
 * @param minUpdateInterval минимальный интервал обновления котировки (независимо от режима доставки)
 */
public record ProviderSettings(
        java.time.Duration minUpdateInterval
) {
    // ↓↓ Базовые проверки аргументов
    public ProviderSettings {
        java.util.Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");
        if (minUpdateInterval.isZero() || minUpdateInterval.isNegative()) {
            throw new IllegalArgumentException("minUpdateInterval must be positive");
        }
    }
}
