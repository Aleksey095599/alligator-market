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

    /* Валюты: */
    CURRENCY_ALREADY_EXISTS(DomainErrorType.CONFLICT),
    CURRENCY_NAME_DUPLICATE(DomainErrorType.CONFLICT),
    CURRENCY_NOT_FOUND(DomainErrorType.NOT_FOUND),
    CURRENCY_IN_USE(DomainErrorType.CONFLICT);


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
