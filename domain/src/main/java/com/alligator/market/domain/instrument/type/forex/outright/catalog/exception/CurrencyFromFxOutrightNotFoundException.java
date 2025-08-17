package com.alligator.market.domain.instrument.type.forex.outright.catalog.exception;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Одна из валют FX_OUTRIGHT не найдена.
 */
public class CurrencyFromFxOutrightNotFoundException extends NotFoundException {
    public CurrencyFromFxOutrightNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
