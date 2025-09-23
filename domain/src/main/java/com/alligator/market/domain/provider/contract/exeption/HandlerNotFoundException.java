package com.alligator.market.domain.provider.contract.exeption;

/**
 * Обработчик для инструмента не найден.
 */
public class HandlerNotFoundException extends RuntimeException {
    public HandlerNotFoundException(String instrumentCode, String providerCode) {
        super("Handler for instrument %s not found in provider %s".formatted(instrumentCode, providerCode));
    }
}
