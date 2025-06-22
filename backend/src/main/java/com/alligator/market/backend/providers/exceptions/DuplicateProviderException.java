package com.alligator.market.backend.providers.exceptions;

/**
 * Очевидно из названия.
 */
public class DuplicateProviderException extends RuntimeException {
    public DuplicateProviderException(String name) {
        super("Provider '%s' already exists".formatted(name));
    }
}
