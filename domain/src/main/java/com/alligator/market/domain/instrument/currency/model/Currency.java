package com.alligator.market.domain.instrument.currency.model;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

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
