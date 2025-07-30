package com.alligator.market.backend.instrument.type.forex.currency_pair.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class PairNotFoundException extends EntityNotFoundException {
    public PairNotFoundException(String pairCode) {
        super("Currency pair '%s' not found".formatted(pairCode));
    }
}
