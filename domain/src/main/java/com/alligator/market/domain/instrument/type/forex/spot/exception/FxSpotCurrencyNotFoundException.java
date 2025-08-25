package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Одна из валют инструмента не найдена.
 */
public class FxSpotCurrencyNotFoundException extends NotFoundException {
    public FxSpotCurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
