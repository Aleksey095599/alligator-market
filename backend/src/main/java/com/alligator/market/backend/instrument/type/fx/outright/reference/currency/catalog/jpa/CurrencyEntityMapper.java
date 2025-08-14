package com.alligator.market.backend.instrument.type.fx.outright.reference.currency.catalog.jpa;

import com.alligator.market.domain.instrument.type.fx.outright.reference.currency.model.Currency;

/**
 * Маппер между сущностью валюты и доменной моделью.
 */
public final class CurrencyEntityMapper {

    private CurrencyEntityMapper() {
    }

    /** Преобразует сущность в доменную модель. */
    public static Currency toDomain(CurrencyEntity entity) {
        return new Currency(
                entity.getCode(),
                entity.getName(),
                entity.getCountry(),
                entity.getDecimal()
        );
    }

    /** Заполняет сущность данными из доменной модели. */
    public static void toEntity(Currency currency, CurrencyEntity entity) {
        entity.setCode(currency.code());
        entity.setName(currency.name());
        entity.setCountry(currency.country());
        entity.setDecimal(currency.decimal());
    }
}

