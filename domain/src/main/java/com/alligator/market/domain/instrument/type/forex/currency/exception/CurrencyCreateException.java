package com.alligator.market.domain.instrument.type.forex.currency.exception;

import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка создания валюты.
 */
public final class CurrencyCreateException extends RuntimeException {

    private final CurrencyCode code;

    /**
     * Создает исключение.
     *
     * @param code код валюты
     */
    @SuppressWarnings("unused")
    public CurrencyCreateException(CurrencyCode code) {
        super(msg(code));
        this.code = code;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param code  код валюты
     * @param cause причина ошибки
     */
    public CurrencyCreateException(CurrencyCode code, Throwable cause) {
        super(msg(code), cause);
        this.code = code;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param code код валюты
     * @return текст сообщения
     */
    private static String msg(CurrencyCode code) {
        CurrencyCode c = Objects.requireNonNull(code, "code must not be null");
        return "Failed to create Currency (code=" + c.value() + ")";
    }

    /**
     * Возвращает код валюты.
     *
     * @return код валюты
     */
    @SuppressWarnings("unused")
    public CurrencyCode getCode() {
        return code;
    }
}
