package com.alligator.market.domain.provider.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение кода провайдера рыночных данных.
 *
 * @param value нормализованный код провайдера
 */
public record ProviderCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 50;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    public ProviderCode {
        value = normalize(value);
    }

    public static ProviderCode of(String raw) {
        return new ProviderCode(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "providerCode must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }

        normalized = normalized.toUpperCase(Locale.ROOT);

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("providerCode length must be <= " + MAX_LENGTH);
        }

        if (!VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "providerCode must match pattern [A-Z0-9_.-]+: '" + normalized + "'"
            );
        }

        return normalized;
    }
}
