package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class SettingsNotFoundException extends EntityNotFoundException {
    public SettingsNotFoundException(String pair, String provider, String mode) {
        super("Streaming settings for pair '%s', provider '%s' and mode '%s' not found".formatted(pair, provider, mode));
    }
}
