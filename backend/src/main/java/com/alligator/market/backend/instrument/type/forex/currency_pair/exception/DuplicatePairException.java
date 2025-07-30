package com.alligator.market.backend.instrument.type.forex.currency_pair.exception;

/**
 * Дублирование валютной пары.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String symbol) {
        super("Currency pair '%s' already exists".formatted(symbol));
    }
}
