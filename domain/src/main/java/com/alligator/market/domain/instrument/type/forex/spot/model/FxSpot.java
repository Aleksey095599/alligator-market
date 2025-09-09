package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;

/**
 * Модель финансового инструмента FX_SPOT.
 */
public record FxSpot(

        Currency base,
        Currency quote,
        ValueDateCode valueDateCode,
        Integer quoteDecimal

) implements Instrument {

    // Проверяем, что валюты отличаются
    public FxSpot {
        if (base.code().equals(quote.code())) {
            throw new FxSpotSameCurrenciesException();
        }
    }

    @Override
    public String code() {
        return base.code() + quote.code() + "_" + valueDateCode;
    }

    @Override
    public InstrumentType type() {
        return InstrumentType.FX_SPOT;
    }
}
