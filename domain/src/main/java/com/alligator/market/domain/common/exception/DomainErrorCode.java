package com.alligator.market.domain.common.exception;

import java.util.Objects;

/**
 * Коды и типы ошибок доменной логики.
 */
public enum DomainErrorCode {

    /* Провайдеры: */
    HANDLER_NOT_FOUND(DomainErrorType.NOT_FOUND);

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
