package com.alligator.market.domain.instrument.asset.forex.reference.currency.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение кода валюты в формате ISO 4217.
 *
 * @param value нормализованный трехбуквенный код валюты
 */
public record CurrencyCode(
        String value
) {

    private static final String PATTERN = "^[A-Z]{3}$";
    private static final int CODE_LENGTH = 3;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    public CurrencyCode {
        value = normalize(value);
    }

    public static CurrencyCode of(String raw) {
        return new CurrencyCode(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "currencyCode must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("currencyCode must not be blank");
        }

        normalized = normalized.toUpperCase(Locale.ROOT);

        if (normalized.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("currencyCode length must be " + CODE_LENGTH);
        }

        if (!VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "currencyCode must match pattern [A-Z]{3}: '" + normalized + "'"
            );
        }

        return normalized;
    }
}
