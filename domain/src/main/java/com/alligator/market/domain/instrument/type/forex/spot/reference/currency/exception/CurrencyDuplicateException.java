package com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception;

/**
 * Валюта уже существует в хранилище.
 */
public class CurrencyDuplicateException extends RuntimeException {
    public CurrencyDuplicateException(String field, String value) {
        super("Currency with %s '%s' already exists".formatted(field, value));
    }
}
