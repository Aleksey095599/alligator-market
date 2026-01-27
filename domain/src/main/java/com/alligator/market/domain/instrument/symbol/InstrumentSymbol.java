package com.alligator.market.domain.instrument.symbol;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение символа инструмента.
 *
 * <p>Причина создания: гарантировать единый формат символа без единообразных проверок.</p>
 *
 * @param value строковое значение символа инструмента
 */
public record InstrumentSymbol(
        String value
) {

    /* Шаблон допустимых символов для "символа инструмента". */
    public static final String PATTERN = "^[A-Z0-9_]+$";

    /* Максимальная длина символа инструмента. */
    private static final int MAX_LENGTH = 50;

    /* Шаблон для валидации символа инструмента. */
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    /**
     * Конструктор.
     */
    public InstrumentSymbol {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового символа.
     */
    public static InstrumentSymbol of(String raw) {
        return new InstrumentSymbol(raw);
    }

    /**
     * Метод проверки и нормализации входящего строкового значения символа инструмента.
     *
     * @param raw исходное строковое значение символа инструмента
     * @return нормализованное значение символа инструмента
     */
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
