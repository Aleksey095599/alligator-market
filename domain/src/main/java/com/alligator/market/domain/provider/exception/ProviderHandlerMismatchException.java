package com.alligator.market.domain.provider.exception;

/**
 * Обработчик относится к другому провайдеру.
 */
public class ProviderHandlerMismatchException extends RuntimeException {
    public ProviderHandlerMismatchException(String providerCode, String handlerCode) {
        super("Handlers list of provider with code %s has at least one handler for another provider with code %s".formatted(providerCode, handlerCode));
    }
}
