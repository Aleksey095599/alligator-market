package com.alligator.market.domain.instrument.type.forex.ref.currency.model;

import java.util.Objects;

/**
 * Базовая модель валюты.
 */
public record Currency(
        /* ↓↓ Базовые атрибуты валюты. */
        String code,
        String name,
        String country,
        Integer decimal
) {
    /** Канонический конструктор с проверками. */
    public Currency {

        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(country, "country must not be null");
        Objects.requireNonNull(decimal, "decimal must not be null");

        if (code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (!code.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("code must match ^[A-Z]{3}$");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (country.isBlank()) {
            throw new IllegalArgumentException("country must not be blank");
        }
    }
}
