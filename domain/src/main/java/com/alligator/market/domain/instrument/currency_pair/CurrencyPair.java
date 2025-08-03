package com.alligator.market.domain.instrument.currency_pair;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;

import java.time.LocalDate;

/**
 * Доменная модель валютной пары.
 */
public record CurrencyPair(

        String base,
        String quote,
        Integer decimal,
        ValueDateCode valueDateCode,
        LocalDate baseValueDate,
        LocalDate quoteValueDate

) implements Instrument {

    @Override
    public String internalCode() {
        return base + quote + "_" + valueDateCode.name();
    }

    @Override
    public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY_PAIR;
    }

    /** Код валютной пары, составленный из базовой и котируемой валют (без учета типа расчетов). */
    public String pairCode() {
        return base + quote;
    }

    /** Дефолтное создание валютной пары. */
    public CurrencyPair(String base, String quote, Integer decimal) {
        this(base, quote, decimal, ValueDateCode.NONE, null, null);
    }
}
