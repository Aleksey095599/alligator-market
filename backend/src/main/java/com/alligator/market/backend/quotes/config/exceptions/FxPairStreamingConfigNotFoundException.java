package com.alligator.market.backend.quotes.config.exceptions;

import jakarta.persistence.EntityNotFoundException;

/* Конфигурация стрима не найдена по паре и провайдеру. */
public class FxPairStreamingConfigNotFoundException extends EntityNotFoundException {
    public FxPairStreamingConfigNotFoundException(String pair, String provider) {
        super("Streaming config for pair '%s' and provider '%s' not found".formatted(pair, provider));
    }
}
