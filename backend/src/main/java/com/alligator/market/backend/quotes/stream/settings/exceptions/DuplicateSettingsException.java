package com.alligator.market.backend.quotes.stream.settings.exceptions;

/**
 * Исключение при попытке создать повторную запись для той же валютный пары и провайдера.
 */
public class DuplicateSettingsException extends RuntimeException {
    public DuplicateSettingsException(String pair, String provider) {
        super("Streaming config for pair '%s' and provider '%s' already exists".formatted(pair, provider));
    }
}
