package com.alligator.market.backend.quote.ccypair_feed_settings.exceptions;

/**
 * Исключение при попытке создать повторную запись для той же валютной пары, провайдера и режима.
 */
public class DuplicateSettingsException extends RuntimeException {
    public DuplicateSettingsException(String pair, String provider) {
        super("Streaming settings for pair '%s' and provider '%s' already exists".formatted(pair, provider));
    }
}
