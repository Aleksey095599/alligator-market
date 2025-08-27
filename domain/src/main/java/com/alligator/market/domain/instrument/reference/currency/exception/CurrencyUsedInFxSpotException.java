package com.alligator.market.domain.instrument.reference.currency.exception;

/**
 * Валюта используется в инструментах FX_SPOT.
 */
public class CurrencyUsedInFxSpotException extends RuntimeException {
    public CurrencyUsedInFxSpotException(String code) {
        super("Currency '%s' used in FX_SPOT instrument".formatted(code));
    }
}
