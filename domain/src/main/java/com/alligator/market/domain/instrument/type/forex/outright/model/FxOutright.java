package com.alligator.market.domain.instrument.type.forex.outright.model;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

public record FxOutright(

        String baseCurrency,
        String quoteCurrency,
        Integer quoteDecimal,
        ValueDateCode valueDateCode

) implements Instrument {

    @Override
    public String internalCode() {
        return baseCurrency + quoteCurrency + "_" + valueDateCode;
    }

    @Override
    public InstrumentType instrumentType() {
        return InstrumentType.FX_OUTRIGHT;
    }
}
