package com.alligator.market.domain.instrument.type.forex.currency.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.type.forex.currency.code.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка удаления валюты.
 */
public final class CurrencyDeleteException extends BaseDomainException {

    private final CurrencyCode code;

    /**
     * Создает исключение.
     *
     * @param code код валюты
     */
    @SuppressWarnings("unused")
    public CurrencyDeleteException(CurrencyCode code) {
        super(DomainErrorCode.CURRENCY_DELETE_FAILED, msg(code));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param code  код валюты
     * @param cause причина ошибки
     */
    public CurrencyDeleteException(CurrencyCode code, Throwable cause) {
        super(DomainErrorCode.CURRENCY_DELETE_FAILED, msg(code), cause);
        this.code = Objects.requireNonNull(code, "code must not be null");
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
