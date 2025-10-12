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

        // Создаем JPA-сущность используя специальный конструктор
        CurrencyEntity e = new CurrencyEntity(c.code());

        apply(c, e); // ← Заполняем изменяемые поля из переданной модели
        return e;
    }

    /** JPA-сущность ⇒ доменная модель. */
    public static Currency toDomain(CurrencyEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // fractionDigits задана как Integer в JPA-сущности и как int в Currency ⇒ нужна null проверка
        int digits = Objects.requireNonNull(e.getFractionDigits(), "fractionDigits must not be null");

        // Собираем и возвращаем доменную модель валюты
        return new Currency(e.getCode(), e.getName(), e.getCountry(), digits);
    }

    /** Копирует в JPA-сущность изменяемые поля из доменной модели. */
    public static void apply(Currency c, CurrencyEntity e) {
        Objects.requireNonNull(c, "model must not be null");
        Objects.requireNonNull(e, "entity must not be null");

        // Модель и сущность должны соответствовать одной и той же валюте
        if (!e.getCode().equals(c.code())) {
            throw new IllegalStateException("Currency code mismatch: " + e.getCode() + " vs " + c.code());
        }

        // ↓↓ Копируем в JPA-сущность поля из доменной модели
        // Код валюты неизменяемый (updatable = false)
        e.setName(c.name());
        e.setCountry(c.country());
        e.setFractionDigits(c.fractionDigits());
    }
}
