package com.alligator.market.domain.common.exception;

import java.io.Serial;
import java.util.Objects;

/**
 * Базовое доменное исключение.
 */
public abstract class BaseDomainException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /* Код доменной ошибки. */
    private final DomainErrorCode errorCode;

    /**
     * Конструктор: создает доменное исключение без причины.
     *
     * @param errorCode код ошибки из {@link DomainErrorCode}
     * @param message   сообщение ошибки
     */
    protected BaseDomainException(DomainErrorCode errorCode, String message) {
        super(message);
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
    }

    /**
     * Конструктор: создает доменное исключение с причиной.
     *
     * @param errorCode код ошибки из {@link DomainErrorCode}
     * @param message   сообщение ошибки
     * @param cause     причина
     */
    protected BaseDomainException(DomainErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
    }

    /**
     * Возвращает код ошибки.
     *
     * @return код ошибки
     */
    @SuppressWarnings("unused")
    public DomainErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Возвращает тип ошибки (извлекается из кода ошибки {@link DomainErrorCode#type()}).
     *
     * @return тип ошибки
     */
    @SuppressWarnings("unused")
    public DomainErrorType getErrorType() {
        return errorCode.type();
    }
}
