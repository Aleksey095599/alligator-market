package com.alligator.market.backend.instrument_catalog.currency_pair.exception;

/**
 * Дублирование валютной пары.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String pairCode) {
        super("Currency pair '%s' already exists".formatted(pairCode));
    }
}
