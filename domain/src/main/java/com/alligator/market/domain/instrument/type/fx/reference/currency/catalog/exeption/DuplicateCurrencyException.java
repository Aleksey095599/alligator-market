package com.alligator.market.domain.instrument.type.fx.reference.currency.catalog.exeption;

/**
 * Валюта уже существует.
 */
public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String field, String value) {
        super("Currency with %s '%s' already exists".formatted(field, value));
    }
}
