package com.alligator.market.domain.instrument.type.forex.ref.currency.exception;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка создания валюты.
 */
public final class CurrencyCreateException extends RuntimeException {

    private final CurrencyCode code;

    private static String msg(CurrencyCode code) {
        CurrencyCode c = Objects.requireNonNull(code, "code must not be null");
        return "Failed to create Currency (code=" + c.value() + ")";
    }

    public CurrencyCreateException(CurrencyCode code) {
        super(msg(code));
        this.code = code;
    }

    public CurrencyCreateException(CurrencyCode code, Throwable cause) {
        super(msg(code), cause);
        this.code = code;
    }

    public CurrencyCode getCode() { return code; }
}
