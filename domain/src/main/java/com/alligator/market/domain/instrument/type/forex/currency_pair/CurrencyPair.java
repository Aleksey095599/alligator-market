package com.alligator.market.domain.instrument.type.forex.currency_pair;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;

/**
 * Доменная модель валютной пары.
 */
public record CurrencyPair(

        /* Код базовой валюты */
        String base,

        /* Код котируемой валюты */
        String quote,

        /* Код пары: base + quote */
        String pairCode,

        /* Кол-во знаков после запятой */
        Integer decimal

) implements Instrument {

    @Override public String symbol() {
        return pairCode;
    }

    @Override public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY_PAIR;
    }
}
