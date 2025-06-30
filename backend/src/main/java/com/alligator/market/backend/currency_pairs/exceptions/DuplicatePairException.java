package com.alligator.market.backend.currency_pairs.exceptions;

/**
 * Очевидно из названия.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String pair) {
        super("Pair '%s' already exists".formatted(pair));
    }
}
