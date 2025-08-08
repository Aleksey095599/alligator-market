package com.alligator.market.domain.instrument.currency_pair.model;

import com.alligator.market.domain.instrument.fxspot.CurrencyValueDateCode;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

import java.time.LocalDate;

/**
 * Модель валютной пары.
 */
public record CurrencyPair(

        String base,
        String quote,
        Integer decimal
) {}
