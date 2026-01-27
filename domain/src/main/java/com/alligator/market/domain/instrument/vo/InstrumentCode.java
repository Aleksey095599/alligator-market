package com.alligator.market.domain.instrument.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение уникального кода инструмента.
 *
 * <p>Причина создания: код инструмента используется многократно в разных слоях приложения,
 * поэтому важно гарантировать единый формат кода без многократных единообразных проверок.</p>
 *
 * @param value строковое значение кода инструмента
 */
public record InstrumentCode(
        String value
) {

    /* Шаблон допустимых символов для кода инструмента. */
    public static final String PATTERN = "^[A-Z0-9_]+$";

    /* Максимальная длина кода инструмента. */
    private static final int MAX_LENGTH = 50;

    /* Шаблон для валидации кода инструмента. */
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    /**
     * Конструктор.
     */
    public InstrumentCode {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового кода.
     */
    public static InstrumentCode of(String raw) {
        return new InstrumentCode(raw);
    }

    /**
     * Метод проверки и нормализации входящего строкового значения кода инструмента.
     *
     * @param raw исходное строковое значение кода инструмента
     * @return нормализованное значение кода инструмента
     */
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
