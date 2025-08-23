package com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception;

/**
 * Валюта используется в инструментах FX_OUTRIGHT.
 */
public class CurrencyUsedInFxOutrightException extends RuntimeException {
    public CurrencyUsedInFxOutrightException(String code) {
        super("Currency '%s' used in FX Outrights".formatted(code));
    }
}
