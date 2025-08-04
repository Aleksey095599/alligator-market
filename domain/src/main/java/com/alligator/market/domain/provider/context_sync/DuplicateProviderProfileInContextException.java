package com.alligator.market.domain.provider.context_sync;

/**
 * Профиль провайдера с указанным полем уже присутствует в контексте.
 */
public class DuplicateProviderProfileInContextException extends RuntimeException {
    public DuplicateProviderProfileInContextException(String field, String value) {
        super("Provider profile with %s '%s' already exists".formatted(field, value));
    }
}
