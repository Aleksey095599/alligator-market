package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.advice;

/**
 * Локальные API-коды ошибок currency feature.
 */
public enum CurrencyApiErrorCode {

    CURRENCY_ALREADY_EXISTS,
    CURRENCY_NAME_DUPLICATE,
    CURRENCY_IN_USE,
    CURRENCY_NOT_FOUND;

    public String code() {
        return name();
    }
}
