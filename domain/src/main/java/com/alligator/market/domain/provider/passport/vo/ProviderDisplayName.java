package com.alligator.market.domain.provider.passport.vo;

import java.util.Objects;

/**
 * Объект-значение отображаемого имени провайдера рыночных данных.
 *
 * @param value непустое имя провайдера для интерфейса и диагностики
 */
public record ProviderDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 50;

    public ProviderDisplayName {
        value = normalize(value);
    }

    public static ProviderDisplayName of(String raw) {
        return new ProviderDisplayName(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "displayName must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("displayName length must be <= " + MAX_LENGTH);
        }

        if (containsControlCharacter(normalized)) {
            throw new IllegalArgumentException("displayName must not contain control characters");
        }

        return normalized;
    }

    private static boolean containsControlCharacter(String value) {
        return value.chars().anyMatch(Character::isISOControl);
    }
}
