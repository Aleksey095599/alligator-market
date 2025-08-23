package com.alligator.market.domain.instrument.type.forex.spot.exception;

/**
 * Базовая и котируемая валюты совпадают.
 */
public class FxOutrightSameCurrenciesException extends RuntimeException {
    public FxOutrightSameCurrenciesException() {
        super("Base and quote currencies must be different");
    }
}

