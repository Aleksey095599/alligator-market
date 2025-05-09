package com.alligator.market.backend.currency.service.exceptions;

/* Исключение для дублирующейся валюты. */
public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String field, String value) {
        super("Currency with " + field + " '" + value + "' already exists");
    }
}