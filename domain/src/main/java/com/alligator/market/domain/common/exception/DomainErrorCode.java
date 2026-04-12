package com.alligator.market.domain.common.exception;

import java.util.Objects;

/**
 * Коды и типы ошибок доменной логики.
 */
public enum DomainErrorCode {

    /* Провайдеры: */
    PROVIDER_REGISTRY_EMPTY(DomainErrorType.BAD_REQUEST),
    PROVIDER_CODE_DUPLICATE(DomainErrorType.CONFLICT),
    PROVIDER_DISPLAY_NAME_DUPLICATE(DomainErrorType.CONFLICT),
    HANDLER_NOT_FOUND(DomainErrorType.NOT_FOUND),
    PROVIDER_ALREADY_ATTACHED(DomainErrorType.CONFLICT),
    PROVIDER_NOT_ATTACHED(DomainErrorType.BAD_REQUEST),
    SUPPORTED_INSTRUMENT_CODES_EMPTY(DomainErrorType.BAD_REQUEST),
    INSTRUMENT_CODE_MISSING(DomainErrorType.BAD_REQUEST),
    INSTRUMENT_NOT_SUPPORTED(DomainErrorType.BAD_REQUEST),
    INSTRUMENT_WRONG_CLASS(DomainErrorType.BAD_REQUEST),
    INSTRUMENT_WRONG_ASSET_CLASS(DomainErrorType.BAD_REQUEST),
    INSTRUMENT_WRONG_CONTRACT_TYPE(DomainErrorType.BAD_REQUEST),

    /* Валюты: */
    CURRENCY_ALREADY_EXISTS(DomainErrorType.CONFLICT),
    CURRENCY_NAME_DUPLICATE(DomainErrorType.CONFLICT),
    CURRENCY_NOT_FOUND(DomainErrorType.NOT_FOUND),
    CURRENCY_IN_USE(DomainErrorType.CONFLICT),
    CURRENCY_CREATE_FAILED(DomainErrorType.TECHNICAL),
    CURRENCY_UPDATE_FAILED(DomainErrorType.TECHNICAL),
    CURRENCY_DELETE_FAILED(DomainErrorType.TECHNICAL),

    /* FOREX_SPOT инструмент: */
    FX_SPOT_ALREADY_EXISTS(DomainErrorType.CONFLICT),
    FX_SPOT_NOT_FOUND(DomainErrorType.NOT_FOUND),
    FX_SPOT_SAME_CURRENCIES(DomainErrorType.BAD_REQUEST),
    FX_SPOT_CREATE_FAILED(DomainErrorType.TECHNICAL),
    FX_SPOT_UPDATE_FAILED(DomainErrorType.TECHNICAL),
    FX_SPOT_DELETE_FAILED(DomainErrorType.TECHNICAL);

    /* Тип ошибки доменной логики. */
    private final DomainErrorType type;

    /**
     * Конструктор.
     */
    DomainErrorCode(DomainErrorType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    /**
     * Возвращает тип ошибки.
     */
    public DomainErrorType type() {
        return type;
    }

    /**
     * Возвращает код ошибки как строковое значение.
     */
    public String code() {
        return name();
    }
}
