package com.alligator.market.backend.instrument.forex.currency_pair.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class PairNotFoundException extends EntityNotFoundException {
    public PairNotFoundException(String pair) {
        super("PairEntity '%s' not found".formatted(pair));
    }
}
