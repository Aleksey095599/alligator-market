package com.alligator.market.domain.provider.exception;

/**
 * Дублирование отображаемых имен провайдеров.
 */
public class ProviderDisplayNameDuplicateException extends RuntimeException {

    public ProviderDisplayNameDuplicateException(String displayName) {
        super("Duplicate provider display name detected: '%s'".formatted(displayName));
    }
}
