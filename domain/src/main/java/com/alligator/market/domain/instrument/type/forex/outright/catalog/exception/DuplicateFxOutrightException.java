package com.alligator.market.domain.instrument.type.forex.outright.catalog.exception;

/**
 * Инструмент уже существует.
 */
public class DuplicateFxOutrightException extends RuntimeException {
    public DuplicateFxOutrightException(String code) {
        super("FxOutright '%s' already exists".formatted(code));
    }
}
