package com.alligator.market.domain.instrument.currency.model;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;

/**
 * Модель валюты.
 */
public record Currency (

        String code,
        String name,
        String country,
        Integer decimal

) implements Instrument {

    @Override public String internalCode() {
        return code;
    }

    @Override public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY;
    }
}
