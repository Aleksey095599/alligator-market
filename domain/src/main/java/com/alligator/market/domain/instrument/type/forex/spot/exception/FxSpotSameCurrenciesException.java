package com.alligator.market.domain.instrument.type.forex.spot.exception;

/**
 * Базовая и котируемая валюты не должны совпадать.
 */
public class FxSpotSameCurrenciesException extends RuntimeException {
    public FxSpotSameCurrenciesException() {
        super("Base and quote currencies must be different");
    }
}

