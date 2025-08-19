package com.alligator.market.domain.instrument.type.forex.outright.model;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.contract.InstrumentType;

/**
 * Модель финансового инструмента FX_OUTRIGHT.
 */
public record FxOutright(

        String baseCurrency,
        String quoteCurrency,
        ValueDateCode valueDateCode,
        Integer quoteDecimal

) implements Instrument {

    @Override
    public String code() {
        return baseCurrency + quoteCurrency + "_" + valueDateCode;
    }

    @Override
    public InstrumentType type() {
        return InstrumentType.FX_OUTRIGHT;
    }
}
