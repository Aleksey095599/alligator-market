package com.alligator.market.domain.instrument.currency.catalog.exeption;

/**
 * Валюта используется в парах.
 */
public class CurrencyUsedInPairsException extends RuntimeException {
    public CurrencyUsedInPairsException(String code) {
        super("Currency '%s' used in pairs".formatted(code));
    }
}
