package com.alligator.market.backend.instrument.forex.currency_pair.exception;

/**
 * Очевидно из названия.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String pair) {
        super("PairEntity '%s' already exists".formatted(pair));
    }
}
