package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Исключение: валюта используется внешними фичами/агрегатами.
 */
public final class CurrencyInUseException extends IllegalStateException {

    /**
     * Создает исключение для используемой валюты.
     *
     * @param currencyCode код валюты
     */
    public CurrencyInUseException(CurrencyCode currencyCode) {
        super("Currency is in use (code=" + Objects.requireNonNull(currencyCode, "currencyCode must not be null").value() + ")");
    }
}
