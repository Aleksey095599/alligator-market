package com.alligator.market.domain.instrument.type.forex.spot.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Коды дат валютирования инструмента FX_SPOT.
 */
public enum FxSpotValueDate {
    /* Константы: коды дат валютирования для инструмента FX_SPOT (далее — коды). */
    TOD, TOM, SPOT;

    /* Коды в виде списка и единой строки (для сообщений об ошибках). */
    private static final List<String> SUPPORTED_CODES = Arrays.stream(values()).map(Enum::name).toList();
    private static final String SUPPORTED_CODES_JOINED = String.join(", ", SUPPORTED_CODES);

    /**
     * Парсит код (trim + upper-case). В ошибке подсказывает допустимые значения.
     */
    public static FxSpotValueDate fromCode(String code) {
        Objects.requireNonNull(code, "FxSpotValueDate code must not be null");

        // Обрезаем пробелы
        String trimmed = code.strip();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("FxSpotValueDate code is blank");
        }

        // Делаем нормализацию регистра
        String normalized = trimmed.toUpperCase(Locale.ROOT);

        // Парсинг
        try {
            return FxSpotValueDate.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unsupported FxSpotValueDate code: '" + code + "'. Supported: " + SUPPORTED_CODES_JOINED, ex);
        }
    }

    /**
     * Возвращает список поддерживаемых кодов (для валидации/документации).
     */
    @SuppressWarnings("unused")
    public static List<String> supportedCodes() {
        return SUPPORTED_CODES;
    }

    /**
     * Возвращает строковый код (= имя константы).
     */
    public String code() {
        return name();
    }
}
