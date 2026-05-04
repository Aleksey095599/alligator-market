package com.alligator.market.domain.instrument.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение уникального внутреннего кода инструмента.
 *
 * @param value нормализованный код инструмента
 */
public record InstrumentCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_]+$";
    private static final int MAX_LENGTH = 50;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    public InstrumentCode {
        value = normalize(value);
    }

    public static InstrumentCode of(String raw) {
        return new InstrumentCode(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "instrumentCode must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("instrumentCode must not be blank");
        }

        normalized = normalized.toUpperCase(Locale.ROOT);

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("instrumentCode length must be <= " + MAX_LENGTH);
        }

        if (!VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "instrumentCode must match pattern [A-Z0-9_]+: '" + normalized + "'"
            );
        }

        return normalized;
    }
}
