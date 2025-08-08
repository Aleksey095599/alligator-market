package com.alligator.market.domain.instrument.fxspot;

import com.alligator.market.domain.instrument.currency_pair.model.CurrencyPair;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

import java.time.LocalDate;

/**
 * Модель инструмента FX_SPOT.
 */
public record FxSpot(

        CurrencyPair currencyPair,
        CurrencyValueDateCode baseValueDate,
        CurrencyValueDateCode quoteValueDate

) implements Instrument {

    @Override
    public String internalCode() {
        return baseCurrency + quoteCurrency + "_" + valueDate.name();
    }

    @Override
    public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY_PAIR;
    }

    //

    /** Код валютной пары, составленный из базовой и котируемой валют (без учета типа расчетов). */
    public String pairCode() {
        return baseCurrency + quoteCurrency;
    }

    /** Дефолтное создание валютной пары. */
    public CurrencyPair(String base, String quote, Integer decimal) {
        this(base, quote, decimal, CurrencyValueDateCode.NONE, null, null);
    }
}
