package com.alligator.market.domain.provider.profile.exeption;

/**
 * Профиль провайдера рыночных данных с указанным полем уже присутствует в контексте.
 */
public class ContextProfileDuplicateException extends RuntimeException {
    public ContextProfileDuplicateException(String field, String value) {
        super("Provider profile with %s '%s' already exists".formatted(field, value));
    }
}
