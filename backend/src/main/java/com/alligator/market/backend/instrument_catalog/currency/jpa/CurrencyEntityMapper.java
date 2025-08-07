package com.alligator.market.backend.instrument_catalog.currency.jpa;

import com.alligator.market.domain.instrument.currency.Currency;

/**
 * Утилитарный класс для преобразования между сущностью {@link CurrencyEntity}
 * и доменной моделью {@link Currency}.
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
}

