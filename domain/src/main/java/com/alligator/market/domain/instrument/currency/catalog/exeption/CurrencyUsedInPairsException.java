package com.alligator.market.domain.instrument.currency.catalog.exeption;

/**
 * Нельзя удалить валюту, если она используется в валютных парах.
 */
public class CurrencyUsedInPairsException extends RuntimeException {
    public CurrencyUsedInPairsException(String code) {
        super("Currency '%s' used in pairs".formatted(code));
    }
}
