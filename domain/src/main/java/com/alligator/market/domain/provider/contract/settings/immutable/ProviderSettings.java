package com.alligator.market.domain.provider.contract.settings.immutable;

import java.util.Objects;

/**
 * Иммутабельные настройки провайдера.
 *
 * @param minUpdateIntervalSec Минимальный интервал обновления котировки (независимо от режима доставки)
 */
public record ProviderSettings(
        Integer minUpdateIntervalSec
) {
    /**
     * Минимально допустимый интервал обновления котировки.
     * 1 секунда защищает от ошибочного указания меньшей размерности и чрезмерной нагрузки.
     */
    private static final Integer MIN_ALLOWED_UPDATE_INTERVAL_SEC = 1;

    public ProviderSettings {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(minUpdateIntervalSec, "minUpdateIntervalSec must not be null");

        if (minUpdateIntervalSec.isZero() || minUpdateIntervalSec.isNegative()) {
            throw new IllegalArgumentException("minUpdateIntervalSec must be positive");
        }
        if (minUpdateIntervalSec.compareTo(MIN_ALLOWED_UPDATE_INTERVAL_SEC) < 0) {
            throw new IllegalArgumentException("minUpdateIntervalSec must be greater than or equal to 1 second");
        }
    }
}
