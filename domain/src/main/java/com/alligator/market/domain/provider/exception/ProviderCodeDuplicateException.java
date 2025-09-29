package com.alligator.market.domain.provider.exception;

/**
 * Дублирование кодов провайдеров.
 */
public class ProviderCodeDuplicateException extends RuntimeException {

    public ProviderCodeDuplicateException(String providerCode) {
        super("Duplicate provider code detected: '%s'".formatted(providerCode));
    }
}
