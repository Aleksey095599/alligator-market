package com.alligator.market.domain.instrument.catalog.forex.fxspot.exception;

import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка: базовая и котируемая валюты совпадают.
 */
public final class FxSpotSameCurrenciesException extends IllegalArgumentException {

    public FxSpotSameCurrenciesException(CurrencyCode currencyCode) {
        super("Base and quote currencies must be different (currency=%s)".formatted(
                Objects.requireNonNull(currencyCode, "currencyCode must not be null").value()
        ));
    }
}
