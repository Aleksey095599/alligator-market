package com.alligator.market.backend.instrument.asset.forex.fxspot.api.exception;

/**
 * API-коды ошибок FOREX_SPOT slice.
 */
public enum FxSpotApiErrorCode {

    FX_SPOT_ALREADY_EXISTS,
    FX_SPOT_CREATE_SAME_CURRENCIES,
    FX_SPOT_NOT_FOUND,
    FX_SPOT_IN_USE,

    // Ошибка reference lookup при создании FOREX_SPOT.
    CURRENCY_NOT_FOUND,

    FX_SPOT_CREATE_FAILED,
    FX_SPOT_UPDATE_FAILED,
    FX_SPOT_DELETE_FAILED;

    public String code() {
        return name();
    }
}
