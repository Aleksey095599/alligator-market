package com.alligator.market.domain.instrument.type.fx.outright.reference.currency_pair.model;

/**
 * Модель валютной пары.
 */
public record CurrencyPair(

        String base,
        String quote,
        Integer decimal
) {

    /** Код валютной пары, составленный из базовой и котируемой валют. */
    public String pairCode() {
        return base + quote;
    }
}
