package com.alligator.market.domain.instrument.asset.forex.fxspot.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка: базовая и котируемая валюты совпадают.
 */
public final class FxSpotSameCurrenciesException extends BaseDomainException {

    /**
     * Создает исключение.
     *
     * @param currencyCode код совпадающей валюты
     */
    public FxSpotSameCurrenciesException(CurrencyCode currencyCode) {
        super(
                DomainErrorCode.FX_SPOT_SAME_CURRENCIES,
                "Base and quote currencies must be different (currency=%s)".formatted(
                        Objects.requireNonNull(currencyCode, "currencyCode must not be null").value()
                )
        );
    }
}
