package com.alligator.market.domain.instrument.type.forex.ref.currency.exception;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка обновления валюты.
 */
public final class CurrencyUpdateException extends RuntimeException {

    private final CurrencyCode code;

    /**
     * Создает исключение.
     *
     * @param code код валюты
     */
    @SuppressWarnings("unused")
    public CurrencyUpdateException(CurrencyCode code) {
        super(msg(code));
        this.code = code;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param code  код валюты
     * @param cause причина ошибки
     */
    public CurrencyUpdateException(CurrencyCode code, Throwable cause) {
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
        return "Failed to update Currency (code=" + c.value() + ")";
    }

    /**
     * Возвращает код валюты.
     *
     * @return код валюты
     */
    public CurrencyCode getCode() {
        return code;
    }
}
