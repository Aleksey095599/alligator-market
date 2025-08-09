package com.alligator.market.domain.instrument.type.fx.spot.model;

import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.model.CurrencyPair;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

/**
 * Модель инструмента FX_SPOT.
 */
public record FxSpot(

        CurrencyPair currencyPair,
        ValueDateCode valueDateCode

) implements Instrument {

    @Override
    public String internalCode() {
        return currencyPair.base() + currencyPair.quote() + "_" + valueDateCode;
    }

    @Override
    public InstrumentType instrumentType() {
        return InstrumentType.FX_SPOT;
    }
}
