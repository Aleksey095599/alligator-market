package com.alligator.market.backend.quotes.config.exceptions;

/* Конфигурация стрима уже существует для указанной пары и провайдера. */
public class DuplicateFxPairStreamingConfigException extends RuntimeException {
    public DuplicateFxPairStreamingConfigException(String pair, String provider) {
        super("Streaming config for pair '%s' and provider '%s' already exists".formatted(pair, provider));
    }
}
