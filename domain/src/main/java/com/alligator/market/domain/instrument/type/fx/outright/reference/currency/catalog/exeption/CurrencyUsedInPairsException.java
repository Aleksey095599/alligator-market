package com.alligator.market.domain.instrument.type.fx.outright.reference.currency.catalog.exeption;

/**
 * Валюта используется в парах.
 */
public class CurrencyUsedInPairsException extends RuntimeException {
    public CurrencyUsedInPairsException(String code) {
        super("Currency '%s' used in pairs".formatted(code));
    }
}
