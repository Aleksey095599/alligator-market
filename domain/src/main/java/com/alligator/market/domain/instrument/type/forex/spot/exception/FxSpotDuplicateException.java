package com.alligator.market.domain.instrument.type.forex.spot.exception;

/**
 * Инструмент уже существует в хранилище.
 */
public class FxSpotDuplicateException extends RuntimeException {
    public FxSpotDuplicateException(String code) {
        super("FX_SPOT instrument '%s' already exists".formatted(code));
    }
}
