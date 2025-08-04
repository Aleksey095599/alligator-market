package com.alligator.market.backend.instrument_catalog.currency.exception;

/**
 * Дублирование валюты.
 */
public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String field, String value) {
        super("Currency with %s '%s' already exists".formatted(field, value));
    }
}
