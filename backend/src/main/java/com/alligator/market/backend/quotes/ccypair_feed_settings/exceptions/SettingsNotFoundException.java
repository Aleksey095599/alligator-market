package com.alligator.market.backend.quotes.ccypair_feed_settings.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class SettingsNotFoundException extends EntityNotFoundException {
    public SettingsNotFoundException(String pair, String provider) {
        super("Streaming settings for pair '%s' and provider '%s' not found".formatted(pair, provider));
    }
}
