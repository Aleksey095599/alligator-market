package com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception;

/**
 * Валюта используется в инструментах FX_OUTRIGHT.
 */
public class CurrencyUsedInFxOutrightsException extends RuntimeException {
    public CurrencyUsedInFxOutrightsException(String code) {
        super("Currency '%s' used in FX Outrights".formatted(code));
    }
}
