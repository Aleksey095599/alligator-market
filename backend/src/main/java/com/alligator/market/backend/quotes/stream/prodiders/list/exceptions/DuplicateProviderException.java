package com.alligator.market.backend.quotes.stream.prodiders.list.exceptions;

/**
 * Очевидно из названия.
 */
public class DuplicateProviderException extends RuntimeException {
    public DuplicateProviderException(String name) {
        super("Provider '%s' already exists".formatted(name));
    }
}
