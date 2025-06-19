package com.alligator.market.backend.quotes.stream.settings.exceptions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Очевидно из названия.
 */
public class SettingsNotFoundException extends EntityNotFoundException {
    public SettingsNotFoundException(String pair, String provider) {
        super("Streaming config for pair '%s' and provider '%s' not found".formatted(pair, provider));
    }
}
