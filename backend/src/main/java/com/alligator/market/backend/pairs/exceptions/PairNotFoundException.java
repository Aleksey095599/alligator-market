package com.alligator.market.backend.pairs.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class PairNotFoundException extends EntityNotFoundException {
    public PairNotFoundException(String pair) {
        super("Pair '%s' not found".formatted(pair));
    }
}
