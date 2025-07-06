package com.alligator.market.backend.instrument.forex.currency.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class CurrencyNotFoundException extends EntityNotFoundException {
    public CurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
