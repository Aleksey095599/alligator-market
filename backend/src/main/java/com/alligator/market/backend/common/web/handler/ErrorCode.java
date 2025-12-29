package com.alligator.market.backend.common.web.handler;

/**
 * Перечень стабильных кодов ошибок для фронта.
 */
public enum ErrorCode {
    /* Общие ошибки: */
    UNEXPECTED_ERROR,
    MALFORMED_JSON,
    BAD_ARGUMENT,
    VALIDATION_ERROR,
    DATA_INTEGRITY_VIOLATION,

    /* Провайдеры: */
    PROVIDER_CODE_DUPLICATE,
    PROVIDER_DISPLAY_NAME_DUPLICATE,
    HANDLER_NOT_FOUND,
    INSTRUMENT_NOT_SUPPORTED,
    INSTRUMENT_WRONG_CLASS,
    INSTRUMENT_WRONG_TYPE,

    /* Валюты: */
    CURRENCY_ALREADY_EXISTS,
    CURRENCY_NAME_DUPLICATE,
    CURRENCY_NOT_FOUND,
    CURRENCY_USED_IN_FX_SPOT,

    /* FX_SPOT инструмент: */
    FX_SPOT_ALREADY_EXISTS,
    FX_SPOT_NOT_FOUND,
    FX_SPOT_SAME_CURRENCIES
}
