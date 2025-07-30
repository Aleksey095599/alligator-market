package com.alligator.market.backend.instrument.type.forex.currency.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Валюта не найдена.
 */
public class CurrencyNotFoundException extends EntityNotFoundException {
    public CurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
