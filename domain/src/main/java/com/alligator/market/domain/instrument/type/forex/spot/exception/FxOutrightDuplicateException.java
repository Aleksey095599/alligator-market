package com.alligator.market.domain.instrument.type.forex.spot.exception;

/**
 * Инструмент уже существует.
 */
public class FxOutrightDuplicateException extends RuntimeException {
    public FxOutrightDuplicateException(String code) {
        super("FxSpot '%s' already exists".formatted(code));
    }
}
