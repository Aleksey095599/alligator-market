package com.alligator.market.backend.provider.profile.exception;

/**
 * Профиль провайдера с указанным полем уже присутствует в контексте.
 */
public class DuplicateProviderProfileException extends RuntimeException {
    public DuplicateProviderProfileException(String field, String value) {
        super("Provider profile with %s '%s' already exists".formatted(field, value));
    }
}
