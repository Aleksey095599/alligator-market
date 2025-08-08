package com.alligator.market.domain.instrument.currency_pair.model;

/**
 * Модель валютной пары.
 */
public record CurrencyPair(

        String base,
        String quote,
        Integer decimal
) {}
