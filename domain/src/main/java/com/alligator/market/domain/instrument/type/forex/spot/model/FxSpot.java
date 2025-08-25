package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Модель финансового инструмента FX_SPOT.
 */
public record FxSpot(

        String baseCurrency,
        String quoteCurrency,
        ValueDateCode valueDateCode,
        Integer quoteDecimal

) implements Instrument {

    @Override
    public String getCode() {
        return baseCurrency + quoteCurrency + "_" + valueDateCode;
    }

    @Override
    public InstrumentType getType() {
        return InstrumentType.FX_SPOT;
    }
}
