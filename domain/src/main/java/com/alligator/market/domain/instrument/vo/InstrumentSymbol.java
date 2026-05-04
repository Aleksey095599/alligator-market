package com.alligator.market.domain.instrument.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение торгового символа инструмента.
 *
 * @param value нормализованный символ инструмента
 */
public record InstrumentSymbol(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_]+$";
    private static final int MAX_LENGTH = 50;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    public InstrumentSymbol {
        value = normalize(value);
    }

    public static InstrumentSymbol of(String raw) {
        return new InstrumentSymbol(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "instrumentSymbol must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("instrumentSymbol must not be blank");
        }

        normalized = normalized.toUpperCase(Locale.ROOT);

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("instrumentSymbol length must be <= " + MAX_LENGTH);
        }

        if (!VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "instrumentSymbol must match pattern [A-Z0-9_]+: '" + normalized + "'"
            );
        }

        return normalized;
    }
}
