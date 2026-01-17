package com.alligator.market.domain.provider.code;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение кода провайдера рыночных данных.
 *
 * <p>Причина создания: код провайдера используется многократно в разных слоях приложения,
 * поэтому важно гарантировать единый формат кода без многократных единообразных проверок.</p>
 *
 * @param value строковое значение кода провайдера
 */
public record ProviderCode(
        String value
) {

    /* Шаблон допустимых символов для кода провайдера. */
    public static final String PATTERN = "^[A-Z0-9_.-]+$";

    /* Максимальная длина кода провайдера. */
    private static final int MAX_LENGTH = 50;

    /* Шаблон для валидации кода провайдера. */
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    /**
     * Конструктор.
     */
    public ProviderCode {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового кода.
     */
    public static ProviderCode of(String raw) {
        return new ProviderCode(raw);
    }

    /**
     * Метод проверки и нормализации входящего значения кода провайдера.
     *
     * @param raw исходное строковое значение кода провайдера
     * @return нормализованное значение кода провайдера
     */
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
