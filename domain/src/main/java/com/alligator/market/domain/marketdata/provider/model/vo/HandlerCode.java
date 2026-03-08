package com.alligator.market.domain.marketdata.provider.model.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение (value object) кода обработчика.
 *
 * <p>Причина создания: код обработчика используется многократно в разных слоях приложения,
 * поэтому важно гарантировать единый формат кода без многократных единообразных проверок.</p>
 *
 * @param value строковое значение кода обработчика
 */
public record HandlerCode(
        String value
) {

    /* Шаблон допустимых символов для кода обработчика. */
    public static final String PATTERN = "^[A-Z0-9_]+$";

    /* Максимальная длина кода обработчика. */
    private static final int MAX_LENGTH = 50;

    /* Шаблон для валидации кода обработчика. */
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    /**
     * Конструктор.
     */
    public HandlerCode {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового кода.
     */
    public static HandlerCode of(String raw) {
        return new HandlerCode(raw);
    }

    /**
     * Метод проверки и нормализации входящего значения кода обработчика.
     *
     * @param raw исходное строковое значение кода обработчика
     * @return нормализованное значение кода обработчика
     */
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
