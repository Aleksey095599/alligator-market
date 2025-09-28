package com.alligator.market.domain.provider.contract.settings;

import java.util.Locale;
import java.util.Objects;

/**
 * Иммутабельные настройки провайдера.
 *
 * @param providerCode      Технический код провайдера
 * @param minUpdateInterval Минимальный интервал обновления котировки (независимо от режима доставки)
 */
public record ProviderSettings(
        String providerCode,
        java.time.Duration minUpdateInterval
) {
    public ProviderSettings {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        java.util.Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        providerCode = providerCode.trim();

        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
        if (providerCode.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("providerCode must not contain whitespaces");
        }
        if (!providerCode.equals(providerCode.toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("providerCode must be upper case");
        }
        if (minUpdateInterval.isZero() || minUpdateInterval.isNegative()) {
            throw new IllegalArgumentException("minUpdateInterval must be positive");
        }
    }
}
