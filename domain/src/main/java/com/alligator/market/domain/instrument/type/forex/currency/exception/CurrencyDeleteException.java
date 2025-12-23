package com.alligator.market.domain.instrument.type.forex.currency.exception;

import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка удаления валюты.
 */
public final class CurrencyDeleteException extends RuntimeException {

    private final CurrencyCode code;

    /**
     * Создает исключение.
     *
     * @param code код валюты
     */
    @SuppressWarnings("unused")
    public CurrencyDeleteException(CurrencyCode code) {
        super(msg(code));
        this.code = code;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param code  код валюты
     * @param cause причина ошибки
     */
    public CurrencyDeleteException(CurrencyCode code, Throwable cause) {
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
        return "Failed to delete Currency (code=" + c.value() + ")";
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
