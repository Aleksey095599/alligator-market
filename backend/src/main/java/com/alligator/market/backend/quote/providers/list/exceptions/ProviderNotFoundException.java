package com.alligator.market.backend.quote.providers.list.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class ProviderNotFoundException extends EntityNotFoundException {
    public ProviderNotFoundException(String name) {
        super("Provider '%s' not found".formatted(name));
    }
}
