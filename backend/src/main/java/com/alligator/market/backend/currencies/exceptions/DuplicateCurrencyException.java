package com.alligator.market.backend.currencies.exceptions;

/**
 * Очевидно из названия.
 */
public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String field, String value) {
        super("Currency with %s '%s' already exists".formatted(field, value));
    }
}