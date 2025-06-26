package com.alligator.market.backend.quotes.stream.providers.list_all.exceptions;

/**
 * Очевидно из названия.
 */
public class DuplicateProviderException extends RuntimeException {
    public DuplicateProviderException(String name) {
        super("Provider '%s' already exists".formatted(name));
    }
}
