package com.alligator.market.domain.provider.context;

/**
 * Профиль провайдера рыночных данных с указанным полем уже присутствует в контексте.
 */
public class DuplicateProfileInContextException extends RuntimeException {
    public DuplicateProfileInContextException(String field, String value) {
        super("Provider profile with %s '%s' already exists".formatted(field, value));
    }
}
