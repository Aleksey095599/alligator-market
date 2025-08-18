package com.alligator.market.domain.instrument.type.forex.outright.catalog.exception;

/**
 * Инструмент уже существует.
 */
public class FxOutrightDuplicateException extends RuntimeException {
    public FxOutrightDuplicateException(String code) {
        super("FxOutright '%s' already exists".formatted(code));
    }
}
