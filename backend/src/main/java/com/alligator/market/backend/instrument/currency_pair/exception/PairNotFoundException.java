package com.alligator.market.backend.instrument.currency_pair.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Валютная пара не найдена.
 */
public class PairNotFoundException extends EntityNotFoundException {
    public PairNotFoundException(String pairCode) {
        super("Currency pair '%s' not found".formatted(pairCode));
    }
}
