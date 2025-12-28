package com.alligator.market.domain.instrument.type.forex.currency.model;

import java.util.Objects;

/**
 * Объект-значение уникального кода валюты.
 *
 * <p>Причина создания – необходимость соблюдения единого формата кода валюты в рамках проекта.</p>
 *
 * @param value строковое значение кода валюты
 */
public record CurrencyCode(
        String value
) {

    /**
     * Конструктор с проверками.
     */
    public CurrencyCode {
        Objects.requireNonNull(value, "Currency code must not be null");
        // Соответствие стандартам ISO-4217
        if (!isIso4217(value)) {
            throw new IllegalArgumentException("Currency code must match [A-Z]{3}");
        }
    }

    /**
     * Фабрика для создания объекта из строкового кода.
     */
    public static CurrencyCode of(String code) {
        return new CurrencyCode(code);
    }

    /**
     * Локальная проверка: три заглавные латинские буквы (согласно стандарту ISO-4217).
     */
    private static boolean isIso4217(String s) {
        return s.length() == 3
                && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z'
                && s.charAt(1) >= 'A' && s.charAt(1) <= 'Z'
                && s.charAt(2) >= 'A' && s.charAt(2) <= 'Z';
    }
}
