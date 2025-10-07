package com.alligator.market.domain.instrument.type.forex.ref.currency.model;

import java.util.Objects;

/**
 * Базовая модель валюты.
 * Используется для создания валютных пар.
 *
 * @param code                    ISO-4217 код валюты.
 * @param name                    Наименование валюты.
 * @param country                 Страна или регион обращения.
 * @param defaultFractionDigits   Количество знаков после запятой для денежных сумм.
 */
public record Currency(
        CurrencyCode code,
        String name,
        String country,
        int defaultFractionDigits
) {
    /** Конструктор с проверками. */
    public Currency (CurrencyCode code, String name, String country, int defaultFractionDigits) {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(country, "country must not be null");

        // ↓↓ Нормализуем и проверяем строковые переменные
        final String nName = name.strip();
        final String nCountry = country.strip();
        if (nName.isEmpty()) throw new IllegalArgumentException("name must not be blank");
        if (nCountry.isEmpty()) throw new IllegalArgumentException("country must not be blank");

        // Ограничение на количество знаков после запятой
        if (defaultFractionDigits < 0 || defaultFractionDigits > 10) {
            throw new IllegalArgumentException("defaultFractionDigits must be between 0 and 10");
        }

        this.code = code;
        this.name = nName;
        this.country = nCountry;
        this.defaultFractionDigits = defaultFractionDigits;
    }

    /** Сравниваем валюты по коду. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency that)) {
            return false;
        }
        return code().equals(that.code());
    }

    /** Хэш по коду. */
    @Override
    public int hashCode() {
        return Objects.hash(code());
    }
}
