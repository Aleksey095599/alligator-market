package com.alligator.market.domain.instrument.type.forex.ref.currency.model;

import java.util.Objects;

/**
 * Базовая модель валюты.
 * Используется для создания валютных пар.
 *
 * @param code             Уникальный код валюты
 * @param name             Наименование валюты
 * @param country          Страна или регион обращения
 * @param fractionDigits   Количество знаков после запятой для денежных сумм
 */
public record Currency(

        CurrencyCode code,
        String name,
        String country,
        int fractionDigits

) {
    /** Конструктор с проверками. */
    public Currency (CurrencyCode code, String name, String country, int fractionDigits) {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(country, "country must not be null");

        // ↓↓ Нормализуем и проверяем строковые переменные
        final String nName = name.strip();
        final String nCountry = country.strip();
        if (nName.isEmpty()) throw new IllegalArgumentException("name must not be blank");
        if (nCountry.isEmpty()) throw new IllegalArgumentException("country must not be blank");

        // Ограничение на стандартное количество знаков после запятой в суммах в данной валюте
        if (fractionDigits < 0 || fractionDigits > 10) {
            throw new IllegalArgumentException("fractionDigits must be between 0 and 10");
        }

        this.code = code;
        this.name = nName;
        this.country = nCountry;
        this.fractionDigits = fractionDigits;
    }
}
