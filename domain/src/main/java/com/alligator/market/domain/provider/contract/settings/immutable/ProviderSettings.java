package com.alligator.market.domain.provider.contract.settings.immutable;

/**
 * Иммутабельные настройки провайдера.
 *
 * @param minUpdateIntervalSec Минимальный интервал обновления котировок в секундах (независимо от режима доставки)
 */
public record ProviderSettings(
        Integer minUpdateIntervalSec
) {
    /* Минимально допустимый интервал обновления котировок в секундах. */
    private static final Integer MIN_ALLOWED_UPDATE_INTERVAL_SEC = 1;

    public ProviderSettings {
        // ↓↓ Базовая валидация аргументов
        if (minUpdateIntervalSec < MIN_ALLOWED_UPDATE_INTERVAL_SEC) {
            throw new IllegalArgumentException("minUpdateIntervalSec must be >= 1 second");
        }
    }
}
