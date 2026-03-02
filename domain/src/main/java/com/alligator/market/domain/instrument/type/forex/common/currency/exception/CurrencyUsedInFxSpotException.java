package com.alligator.market.domain.instrument.type.forex.common.currency.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.type.forex.common.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка: валюта используется в FX_SPOT инструменте.
 */
public final class CurrencyUsedInFxSpotException extends BaseDomainException {

    private final CurrencyCode code;

    /**
     * Создает исключение.
     *
     * @param code код валюты
     */
    public CurrencyUsedInFxSpotException(CurrencyCode code) {
        super(DomainErrorCode.CURRENCY_USED_IN_FX_SPOT, msg(code));
        this.code = Objects.requireNonNull(code, "code must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param code  код валюты
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public CurrencyUsedInFxSpotException(CurrencyCode code, Throwable cause) {
        super(DomainErrorCode.CURRENCY_USED_IN_FX_SPOT, msg(code), cause);
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
        return "Currency used in FX_SPOT instrument (code=" + c.value() + ")";
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
