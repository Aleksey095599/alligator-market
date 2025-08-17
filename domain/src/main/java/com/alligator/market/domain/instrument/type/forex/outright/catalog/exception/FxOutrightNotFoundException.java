package com.alligator.market.domain.instrument.type.forex.outright.catalog.exception;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Инструмент не найден.
 */
public class FxOutrightNotFoundException extends NotFoundException {
    public FxOutrightNotFoundException(String code) {
        super("FxOutright '%s' not found".formatted(code));
    }
}
