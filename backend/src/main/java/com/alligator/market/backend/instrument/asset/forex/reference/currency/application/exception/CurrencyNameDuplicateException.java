package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception;

import java.util.Objects;

public final class CurrencyNameDuplicateException extends IllegalStateException {
    public CurrencyNameDuplicateException(String name) {
        super("Currency with the same name already exists (name='" + Objects.requireNonNull(name,
                "name must not be null") + "')");
    }
}
