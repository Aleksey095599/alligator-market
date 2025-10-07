package com.alligator.market.domain.instrument.type.forex.ref.currency.model;

import java.util.Objects;

/**
 * Базовая модель валюты.
 */
public record Currency(
        CurrencyCode code,
        String name,
        String country,
        Integer decimal
) {
    /** Конструктор с проверками. */
    public Currency (CurrencyCode code, String name, String country, Integer decimal) {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(country, "country must not be null");
        Objects.requireNonNull(decimal, "decimal must not be null");

        // ↓↓ Нормализуем и проверяем строковые переменные
        final String nName = name.strip();
        final String nCountry = country.strip();
        if (nName.isEmpty()) throw new IllegalArgumentException("name must not be blank");
        if (nCountry.isEmpty()) throw new IllegalArgumentException("country must not be blank");

        // Ограничение на количество знаков после запятой
        if (decimal < 0 || decimal > 10) {
            throw new IllegalArgumentException("decimal must be between 0 and 10");
        }

        this.code = code;
        this.name = nName;
        this.country = nCountry;
        this.decimal = decimal;
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
