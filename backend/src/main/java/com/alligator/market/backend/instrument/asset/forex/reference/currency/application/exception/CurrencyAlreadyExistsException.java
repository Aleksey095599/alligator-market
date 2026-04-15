package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/* Бизнес-ошибка: валюта с таким кодом уже существует. */
public final class CurrencyAlreadyExistsException extends IllegalStateException {

    public CurrencyAlreadyExistsException(CurrencyCode currencyCode) {
        super("Currency already exists (code=" + Objects.requireNonNull(currencyCode,
                "currencyCode must not be null").value() + ")");
    }
}
