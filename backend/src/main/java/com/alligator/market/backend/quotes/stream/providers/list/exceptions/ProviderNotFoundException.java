package com.alligator.market.backend.quotes.stream.providers.list.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class ProviderNotFoundException extends EntityNotFoundException {
    public ProviderNotFoundException(String name, String mode) {
        super("Provider '%s' with mode '%s' not found".formatted(name, mode));
    }
}
