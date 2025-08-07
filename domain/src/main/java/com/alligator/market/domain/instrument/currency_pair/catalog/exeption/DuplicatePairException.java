package com.alligator.market.domain.instrument.currency_pair.catalog.exeption;

/**
 * Дублирование валютной пары.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String pairCode) {
        super("Currency pair '%s' already exists".formatted(pairCode));
    }
}
