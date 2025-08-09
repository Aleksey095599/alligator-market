package com.alligator.market.domain.instrument.type.fx.reference.currency_pair.model;

/**
 * Модель валютной пары.
 */
public record CurrencyPair(

        String base,
        String quote,
        Integer decimal
) {}
