package com.alligator.market.backend.instrument.type.forex.currency_pair.exception;

/**
 * Очевидно из названия.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String symbol) {
        super("Currency pair '%s' already exists".formatted(symbol));
    }
}
