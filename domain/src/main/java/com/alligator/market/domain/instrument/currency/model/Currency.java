package com.alligator.market.domain.instrument.currency.model;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;

/**
 * Модель валюты.
 */
public record Currency (

        String code,
        String name,
        String country,
        Integer decimal
) {}
