package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Инструмент не найден.
 */
public class FxOutrightNotFoundException extends NotFoundException {
    public FxOutrightNotFoundException(String code) {
        super("FxSpot '%s' not found".formatted(code));
    }
}
