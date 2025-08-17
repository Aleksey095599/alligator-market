package com.alligator.market.domain.instrument.type.forex.outright.catalog.exception;

/**
 * Базовая и котируемая валюты совпадают.
 */
public class SameCurrenciesException extends RuntimeException {
    public SameCurrenciesException() {
        super("Base and quote currencies must be different");
    }
}

