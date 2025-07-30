package com.alligator.market.domain.instrument.type.forex.currency_pair;

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
        SettlementType settlementType,
        LocalDate baseSettlementDate,
        LocalDate quoteSettlementDate

) implements Instrument {

    /** Дефолтное создание валютной пары */
    public CurrencyPair(String base, String quote, Integer decimal) {
        this(base, quote, decimal, SettlementType.TOM, null, null);
    }

    @Override public String symbol() {
        return base + quote;
    }

    @Override public InstrumentType instrumentType() {
        return InstrumentType.CURRENCY_PAIR;
    }
}
