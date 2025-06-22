package com.alligator.market.backend.providers.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class ProviderNotFoundException extends EntityNotFoundException {
    public ProviderNotFoundException(String name) {
        super("Provider '%s' not found".formatted(name));
    }
}
