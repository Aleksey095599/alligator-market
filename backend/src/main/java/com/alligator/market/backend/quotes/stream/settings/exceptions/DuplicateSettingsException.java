package com.alligator.market.backend.quotes.stream.settings.exceptions;

/**
 * Исключение при попытке создать повторную запись для той же валютной пары, провайдера и режима.
 */
public class DuplicateSettingsException extends RuntimeException {
    public DuplicateSettingsException(String pair, String provider, String mode) {
        super("Streaming config for pair '%s', provider '%s' and mode '%s' already exists".formatted(pair, provider, mode));
    }
}
