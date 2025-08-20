package com.alligator.market.domain.provider.exception;

/**
 * Некорректный набор обработчиков провайдера.
 */
public class ProviderHandlersInvalidException extends RuntimeException {
    public ProviderHandlersInvalidException() {
        super("Handlers list must be non-empty and contain no null elements");
    }
}
