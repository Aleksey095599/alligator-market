package com.alligator.market.domain.common.exception;

import java.util.Objects;

/**
 * Базовое доменное исключение с кодом ошибки и типом.
 */
public abstract class BaseDomainException extends RuntimeException {

    private final DomainErrorCodes errorCode;
    private final DomainErrorType errorType;

    /**
     * Создает доменное исключение.
     *
     * @param errorCode код ошибки
     * @param errorType тип ошибки
     * @param message   сообщение
     */
    protected BaseDomainException(String errorCode, DomainErrorType errorType, String message) {
        super(message);
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
        this.errorType = Objects.requireNonNull(errorType, "errorType must not be null");
    }

    /**
     * Создает доменное исключение с причиной.
     *
     * @param errorCode код ошибки
     * @param errorType тип ошибки
     * @param message   сообщение
     * @param cause     причина
     */
    protected BaseDomainException(String errorCode, DomainErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
        this.errorType = Objects.requireNonNull(errorType, "errorType must not be null");
    }

    /**
     * Возвращает код ошибки.
     *
     * @return код ошибки
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Возвращает тип ошибки.
     *
     * @return тип ошибки
     */
    public DomainErrorType getErrorType() {
        return errorType;
    }
}
