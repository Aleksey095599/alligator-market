package com.alligator.market.backend.currency.service;

/* Исключение для дублирующейся валюты по коду. */
public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String field, String value) {
        super("Currency with " + field + " '" + value + "' already exists");
    }
}