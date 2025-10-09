package com.alligator.market.domain.instrument.type.forex.ref.currency.exception;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка обновления валюты.
 */
public final class CurrencyUpdateException extends RuntimeException {

    private final CurrencyCode code;

    private static String msg(CurrencyCode code) {
        CurrencyCode c = Objects.requireNonNull(code, "code must not be null");
        return "Failed to update Currency (code=" + c.value() + ")";
    }

    public CurrencyUpdateException(CurrencyCode code) {
        super(msg(code));
        this.code = code;
    }

    public CurrencyUpdateException(CurrencyCode code, Throwable cause) {
        super(msg(code), cause);
        this.code = code;
    }

    public CurrencyCode getCode() { return code; }
}
