package com.alligator.market.domain.common.exception;

/**
 * Тип ошибки доменной логики для выбора HTTP-статуса.
 */
public enum DomainErrorType {
    BAD_REQUEST,
    NOT_FOUND,
    CONFLICT,
    TECHNICAL
}
