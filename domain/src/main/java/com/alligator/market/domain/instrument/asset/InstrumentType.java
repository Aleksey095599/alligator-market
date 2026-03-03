package com.alligator.market.domain.instrument.asset;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Типы финансовых инструментов.
 */
public enum InstrumentType {
    /* Поддерживаемые типы инструментов (константы). */
    FX_SPOT, FX_SWAP;

    /* Список всех типов и представление этого списка в виде единой строки (иногда полезно). */
    private static final List<String> SUPPORTED_TYPES = Arrays.stream(values()).map(Enum::name).toList();
    private static final String SUPPORTED_TYPES_JOINED = String.join(", ", SUPPORTED_TYPES);

    /**
     * Парсит тип из строкового значения (trim + upper-case).
     */
    @SuppressWarnings("unused")
    public static InstrumentType fromCode(String code) {
        Objects.requireNonNull(code, "InstrumentType code must not be null");

        // Обрезаем пробелы
        String trimmed = code.strip();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("InstrumentType code is blank");
        }

        // Делаем нормализацию регистра
        String normalized = trimmed.toUpperCase(Locale.ROOT);

        // Парсинг
        try {
            return InstrumentType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unsupported InstrumentType code: '" + code + "'. Supported: " + SUPPORTED_TYPES_JOINED, ex);
        }
    }

    /**
     * Возвращает список поддерживаемых типов (для валидации/документации).
     */
    @SuppressWarnings("unused")
    public static List<String> supportedCodes() {
        return SUPPORTED_TYPES;
    }

    /**
     * Возвращает тип как строковое значение.
     */
    public String code() {
        return name();
    }
}
