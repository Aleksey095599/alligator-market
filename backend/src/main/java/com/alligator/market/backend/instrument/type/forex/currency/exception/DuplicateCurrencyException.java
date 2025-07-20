package com.alligator.market.backend.instrument.type.forex.currency.exception;

/**
 * Очевидно из названия.
 */
public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String field, String value) {
        super("Currency with %s '%s' already exists".formatted(field, value));
    }
}