package com.alligator.market.domain.instrument.type.forex.currency;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Доменная модель валюты.
 */
public record Currency (

        String code,
        String name,
        String country,
        Integer decimal

) implements Instrument {

    @Override public String symbol() {
        return code;
    }

    @Override public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY;
    }
}
