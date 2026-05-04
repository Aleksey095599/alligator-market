package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: создание FX_SPOT с одинаковыми базовой и котируемой валютами.
 */
public final class FxSpotCreateSameCurrenciesException extends IllegalArgumentException {

    private final CurrencyCode currencyCode;

    public FxSpotCreateSameCurrenciesException(CurrencyCode currencyCode) {
        super("FX_SPOT create failed: base and quote currencies must be different (currency=%s)".formatted(
                Objects.requireNonNull(currencyCode, "currencyCode must not be null").value()
        ));
        this.currencyCode = currencyCode;
    }

    public CurrencyCode currencyCode() {
        return currencyCode;
    }
}
