package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;

/**
 * Модель финансового инструмента FX_SPOT.
 */
public record FxSpot(

        String baseCurrency,
        String quoteCurrency,
        ValueDateCode valueDateCode,
        Integer quoteDecimal

) implements Instrument {

    // Проверяем, что валюты отличаются
    public FxSpot {
        if (baseCurrency.equals(quoteCurrency)) {
            throw new FxSpotSameCurrenciesException();
        }
    }

    @Override
    public String getCode() {
        return baseCurrency + quoteCurrency + "_" + valueDateCode;
    }

    @Override
    public InstrumentType getType() {
        return InstrumentType.FX_SPOT;
    }
}
