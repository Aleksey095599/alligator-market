package com.alligator.market.backend.currency.service;

public class DuplicateCurrencyException extends RuntimeException {
    public DuplicateCurrencyException(String code) {
        super("Currency with code %s already exists".formatted(code));
    }
}