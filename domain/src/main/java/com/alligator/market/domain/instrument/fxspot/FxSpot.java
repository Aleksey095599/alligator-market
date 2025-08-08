package com.alligator.market.domain.instrument.fxspot;

import com.alligator.market.domain.instrument.currency_pair.model.CurrencyPair;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

/**
 * Модель инструмента FX_SPOT.
 */
public record FxSpot(

        CurrencyPair currencyPair,
        CurrencyValueDate baseValueDate,
        CurrencyValueDate quoteValueDate

) implements Instrument {

    @Override
    public String internalCode() {
        return currencyPair.base() + currencyPair.quote() + "_" + valueDate;
    }

    @Override
    public InstrumentType instrumentType() {
        return InstrumentType.FX_SPOT;
    }

    public FxSpotValueDate valueDate() {
        // если baseValueDate = quoteValueDate тогда valueDate = baseValueDate
        // иначе valueDate = FxSpotValueDate.SPLIT
    }
}
