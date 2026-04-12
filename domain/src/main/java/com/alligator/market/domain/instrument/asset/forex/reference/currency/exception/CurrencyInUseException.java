package com.alligator.market.domain.instrument.asset.forex.reference.currency.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка: валюта используется внешними фичами/агрегатами.
 */
public final class CurrencyInUseException extends BaseDomainException {

    private final CurrencyCode code;

    /**
     * Создает исключение.
     *
     * @param code код валюты
     */
    public CurrencyInUseException(CurrencyCode code) {
        super(DomainErrorCode.CURRENCY_IN_USE, msg(code));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param code  код валюты
     * @param cause причина ошибки
     */
    public CurrencyInUseException(CurrencyCode code, Throwable cause) {
        super(DomainErrorCode.CURRENCY_IN_USE, msg(code), cause);
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
        return "Currency is in use (code=" + c.value() + ")";
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
