package com.alligator.market.domain.common.exception;

/**
 * Базовое исключение для случаев, когда ресурс не найден.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
