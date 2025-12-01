package com.alligator.market.domain.instrument.type;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * <b>Коды типов финансовых инструментов.</b>
 */
public enum InstrumentType {
    /* Константы: коды типов инструментов (далее — коды). */
    FX_SPOT, FX_SWAP;

    /* Коды в виде списка и единой строки (для сообщений об ошибках). */
    private static final List<String> SUPPORTED_CODES = Arrays.stream(values()).map(Enum::name).toList();
    private static final String SUPPORTED_CODES_JOINED = String.join(", ", SUPPORTED_CODES);

    /**
     * Парсит код (trim + upper-case). В ошибке подсказывает допустимые значения.
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
                    "Unsupported InstrumentType code: '" + code + "'. Supported: " + SUPPORTED_CODES_JOINED, ex);
        }
    }

    /**
     * <b>Возвращает список поддерживаемых кодов (для валидации/документации).</b>
     */
    @SuppressWarnings("unused")
    public static List<String> supportedCodes() {
        return SUPPORTED_CODES;
    }

    /**
     * <b>Возвращает строковый код (= имя константы).</b>
     */
    public String code() {
        return name();
    }
}
