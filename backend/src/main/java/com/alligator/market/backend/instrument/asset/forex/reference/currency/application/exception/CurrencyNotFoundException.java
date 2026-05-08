package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

public final class CurrencyNotFoundException extends IllegalStateException {
    public CurrencyNotFoundException(CurrencyCode currencyCode) {
        super("Currency not found (code=" + Objects.requireNonNull(currencyCode,
                "currencyCode must not be null").value() + ")");
    }
}
