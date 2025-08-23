package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Одна из валют FX_OUTRIGHT не найдена.
 */
public class FxOutrightCurrencyNotFoundException extends NotFoundException {
    public FxOutrightCurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
