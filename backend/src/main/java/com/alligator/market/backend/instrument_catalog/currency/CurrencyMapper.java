package com.alligator.market.backend.instrument_catalog.currency;

import com.alligator.market.backend.instrument_catalog.currency.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.currency.Currency;

/**
 * Утилитарный класс для преобразования между доменной моделью
 * {@link Currency} и сущностью {@link CurrencyEntity}.
 */
public final class CurrencyMapper {

    private CurrencyMapper() {
    }

    /**
     * Заполняет сущность данными из доменной модели.
     */
    public static void toEntity(Currency currency, CurrencyEntity entity) {
        entity.setCode(currency.code());
        entity.setName(currency.name());
        entity.setCountry(currency.country());
        entity.setDecimal(currency.decimal());
    }

    /**
     * Преобразует доменную модель в новую сущность.
     */
    public static CurrencyEntity toEntity(Currency currency) {
        var entity = new CurrencyEntity();
        toEntity(currency, entity);
        return entity;
    }

    /**
     * Преобразует сущность в доменную модель.
     */
    public static Currency toDomain(CurrencyEntity entity) {
        return new Currency(
                entity.getCode(),
                entity.getName(),
                entity.getCountry(),
                entity.getDecimal()
        );
    }
}

