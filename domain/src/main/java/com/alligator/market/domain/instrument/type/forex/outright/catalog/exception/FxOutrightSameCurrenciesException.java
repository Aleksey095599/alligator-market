package com.alligator.market.domain.instrument.type.forex.outright.catalog.exception;

/**
 * Базовая и котируемая валюты совпадают.
 */
public class FxOutrightSameCurrenciesException extends RuntimeException {
    public FxOutrightSameCurrenciesException() {
        super("Base and quote currencies must be different");
    }
}

