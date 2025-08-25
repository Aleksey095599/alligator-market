package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Инструмент не найден в хранилище.
 */
public class FxSpotNotFoundException extends NotFoundException {
    public FxSpotNotFoundException(String code) {
        super("FX_SPOT instrument '%s' not found".formatted(code));
    }
}
