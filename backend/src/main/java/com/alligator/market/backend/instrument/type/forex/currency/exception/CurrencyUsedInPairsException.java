package com.alligator.market.backend.instrument.type.forex.currency.exception;

/**
 * Нельзя удалить валюту, если она используется в валютных парах.
 */
public class CurrencyUsedInPairsException extends RuntimeException {
    public CurrencyUsedInPairsException(String code) {
        super("Currency '%s' used in pairs".formatted(code));
    }
}
