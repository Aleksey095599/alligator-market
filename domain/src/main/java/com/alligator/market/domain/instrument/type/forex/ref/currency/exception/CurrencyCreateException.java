package com.alligator.market.domain.instrument.type.forex.ref.currency.exception;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

/** Неустановленная ошибка создания валюты. */
public class CurrencyCreateException extends RuntimeException {

    private final CurrencyCode code;

    public CurrencyCreateException(CurrencyCode code, Throwable cause) {
        super("Failed to create currency: " + code, cause);
        this.code = code;
    }

    public CurrencyCode code() { return code; }
}
