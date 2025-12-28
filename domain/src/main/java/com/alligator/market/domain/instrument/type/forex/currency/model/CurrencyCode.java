package com.alligator.market.domain.instrument.type.forex.currency.model;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение уникального кода валюты.
 *
 * <p>Причина создания – необходимость соблюдения единого формата кода валюты в рамках проекта.</p>
 *
 * @param value строковое значение кода валюты
 */
public record CurrencyCode(
        String value
) {

    /* Шаблон допустимых символов для кода валюты. */
    public static final String PATTERN = "^[A-Z]{3}$";

    private static final int CODE_LENGTH = 3;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    /**
     * Конструктор.
     */
    public CurrencyCode {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового кода.
     */
    public static CurrencyCode of(String raw) {
        return new CurrencyCode(raw);
    }

    /**
     * Метод проверки и нормализации входящего значения кода валюты.
     *
     * @param raw исходное строковое значение кода валюты
     * @return нормализованное значение кода валюты
     */
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
