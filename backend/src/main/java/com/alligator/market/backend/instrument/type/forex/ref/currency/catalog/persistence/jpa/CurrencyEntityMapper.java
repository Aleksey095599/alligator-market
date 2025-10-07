package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import java.util.Objects;

/**
 * Маппер: JPA-сущность ⇄ доменная модель.
 */
public final class CurrencyEntityMapper {

    /** Приватный конструктор (запрещает создание экземпляров). */
    private CurrencyEntityMapper() { throw new UnsupportedOperationException("Utility class"); }

    /** Создать новую JPA-сущность из доменной модели. */
    public static CurrencyEntity newEntity(Currency c) {
        Objects.requireNonNull(c, "model must not be null");

        // Устанавливаем natural id (code) через конструктор.
        // После создания менять нельзя (нет сеттера, updatable=false).
        var e = new CurrencyEntity(c.code());

        apply(c, e); // ← Копируем прочие поля из модели
        return e;
    }

    /** JPA-сущность ⇒ доменная модель. */
    public static Currency toDomain(CurrencyEntity e) {
        Objects.requireNonNull(e, "entity must not be null");
        Objects.requireNonNull(e.getCode(), "code must not be null");
        int digits = Objects.requireNonNull(e.getDefaultFractionDigits(),
                "defaultFractionDigits must not be null");

        return new Currency(e.getCode(), e.getName(), e.getCountry(), digits);
    }

    /** Копирует в JPA-сущность изменяемые поля из доменной модели. */
    public static void apply(Currency c, CurrencyEntity e) {
        Objects.requireNonNull(c, "model must not be null");
        Objects.requireNonNull(e, "entity must not be null");
        Objects.requireNonNull(e.getCode(), "entity code must not be null");

        // Модель и сущность должны соответствовать одной валюте
        if (!e.getCode().equals(c.code())) {
            throw new IllegalStateException("Currency code mismatch: " + e.getCode() + " vs " + c.code());
        }

        // Код валюты неизменяемый (updatable = false)
        e.setName(c.name());
        e.setCountry(c.country());
        e.setDefaultFractionDigits(c.defaultFractionDigits());
    }
}
