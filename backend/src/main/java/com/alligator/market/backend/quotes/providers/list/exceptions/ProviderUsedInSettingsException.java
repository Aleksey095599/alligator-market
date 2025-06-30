package com.alligator.market.backend.quotes.providers.list.exceptions;

/**
 * Нельзя удалить провайдера, если он используется в настройках потоков котировок.
 */
public class ProviderUsedInSettingsException extends RuntimeException {
    public ProviderUsedInSettingsException(String name) {
        super("Provider '%s' used in feed settings".formatted(name));
    }
}
