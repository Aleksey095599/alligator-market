package com.alligator.market.domain.provider.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение кода обработчика провайдера.
 *
 * @param value нормализованный код обработчика
 */
public record HandlerCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_]+$";
    private static final int MAX_LENGTH = 50;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    public HandlerCode {
        value = normalize(value);
    }

    public static HandlerCode of(String raw) {
        return new HandlerCode(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "handlerCode must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("handlerCode must not be blank");
        }

        normalized = normalized.toUpperCase(Locale.ROOT);

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("handlerCode length must be <= " + MAX_LENGTH);
        }

        if (!VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "handlerCode must match pattern [A-Z0-9_]+: '" + normalized + "'"
            );
        }

        return normalized;
    }
}
