package com.alligator.market.backend.instrument_catalog.currency_pair.jpa;

import com.alligator.market.backend.instrument_catalog.currency.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.currency_pair.CurrencyPair;

/**
 * Утилитарный класс для преобразования между сущностью {@link CurrencyPairEntity}
 * и доменной моделью {@link CurrencyPair}.
 */
public final class CurrencyPairEntityMapper {

    private CurrencyPairEntityMapper() {
    }

    /** Преобразует сущность в доменную модель. */
    public static CurrencyPair toDomain(CurrencyPairEntity entity) {
        return new CurrencyPair(
                entity.getBase().getCode(),
                entity.getQuote().getCode(),
                entity.getDecimal()
        );
    }

    /** Заполняет сущность данными из доменной модели. */
    public static void toEntity(
            CurrencyPair pair,
            CurrencyEntity base,
            CurrencyEntity quote,
            CurrencyPairEntity entity
    ) {
        entity.setBase(base);
        entity.setQuote(quote);
        entity.setPairCode(pair.pairCode());
        entity.setDecimal(pair.decimal());
    }
}

