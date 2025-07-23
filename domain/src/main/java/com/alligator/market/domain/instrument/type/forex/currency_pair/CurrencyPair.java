package com.alligator.market.domain.instrument.type.forex.currency_pair;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;

/**
 * Доменная модель валютной пары.
 */
public record CurrencyPair(

        String code1,
        String code2,
        String pair,
        Integer decimal

) implements Instrument {

    @Override public String symbol() {
        return code1 + code2;
    }

    @Override public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY_PAIR;
    }
}
