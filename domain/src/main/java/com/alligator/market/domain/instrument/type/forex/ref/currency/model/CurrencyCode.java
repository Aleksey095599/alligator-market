package com.alligator.market.domain.instrument.type.forex.ref.currency.model;

import java.util.Objects;

/**
 * Модель кода валюты (ISO-4217).
 * Причина создания модели - необходимость соблюдения единого формата кода валюты в разных слоях проекта.
 *
 * @param value Строковое значение кода валюты
 */
public record CurrencyCode(
        String value
) {

    /** Локальная проверка: три заглавные латинские буквы. */
    private static boolean isIso4217(String s) {
        return s.length() == 3
                && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z'
                && s.charAt(1) >= 'A' && s.charAt(1) <= 'Z'
                && s.charAt(2) >= 'A' && s.charAt(2) <= 'Z';
    }

    /** Конструктор с проверками. */
    public CurrencyCode {
        Objects.requireNonNull(value, "Currency code must not be null");
        if (!isIso4217(value)) {
            throw new IllegalArgumentException("Currency code must match [A-Z]{3}");
        }
    }

    /** Фабрика для создания модели из строкового кода. */
    public static CurrencyCode of(String code) { return new CurrencyCode(code); }

    /** Возвращает строковое представление кода. */
    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() { return value; }
}
