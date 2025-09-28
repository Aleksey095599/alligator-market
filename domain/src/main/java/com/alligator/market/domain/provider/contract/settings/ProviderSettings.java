package com.alligator.market.domain.provider.contract.settings;

import java.time.Duration;
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
        Duration minUpdateInterval
) {
    /**
     * Минимально допустимый интервал обновления котировки.
     * 1 секунда защищает от ошибочного указания меньшей размерности и чрезмерной нагрузки.
     */
    private static final Duration MIN_ALLOWED_UPDATE_INTERVAL = Duration.ofSeconds(1);

    public ProviderSettings {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

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
        if (minUpdateInterval.compareTo(MIN_ALLOWED_UPDATE_INTERVAL) < 0) {
            throw new IllegalArgumentException("minUpdateInterval must be greater or equal to PT1S");
        }
    }
}
