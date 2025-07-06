package com.alligator.market.domain.instrument.forex.currency_pair;

/**
 * Доменная модель валютной пары.
 */
public record CurrencyPair(
        String code1,
        String code2,
        String pair,
        Integer decimal
) {}
