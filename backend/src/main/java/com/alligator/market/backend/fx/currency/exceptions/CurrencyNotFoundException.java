package com.alligator.market.backend.fx.currency.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class CurrencyNotFoundException extends EntityNotFoundException {
    public CurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
