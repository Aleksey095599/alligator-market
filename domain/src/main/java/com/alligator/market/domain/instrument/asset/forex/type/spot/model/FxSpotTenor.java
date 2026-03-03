package com.alligator.market.domain.instrument.asset.forex.type.spot.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Теноры дат валютирования инструмента FX_SPOT.
 */
public enum FxSpotTenor {
    /* Константы: теноры дат валютирования для инструмента FX_SPOT (далее – теноры). */
    TOD, TOM, SPOT;

    /* Теноры в виде списка и единой строки (для сообщений об ошибках). */
    private static final List<String> SUPPORTED_TENORS = Arrays.stream(values()).map(Enum::name).toList();
    private static final String SUPPORTED_TENORS_JOINED = String.join(", ", SUPPORTED_TENORS);

    /**
     * Парсит тенор (trim + upper-case). В ошибке подсказывает допустимые значения.
     */
    public static FxSpotTenor fromValue(String value) {
        Objects.requireNonNull(value, "FxSpotTenor value must not be null");

        // Обрезаем пробелы
        String trimmed = value.strip();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("FxSpotTenor value is blank");
        }

        // Делаем нормализацию регистра
        String normalized = trimmed.toUpperCase(Locale.ROOT);

        // Парсинг
        try {
            return FxSpotTenor.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unsupported FxSpotTenor value: '" + value + "'. Supported: " + SUPPORTED_TENORS_JOINED, ex);
        }
    }

    /**
     * Возвращает список поддерживаемых теноров (для валидации/документации).
     */
    @SuppressWarnings("unused")
    public static List<String> supportedTenors() {
        return SUPPORTED_TENORS;
    }

    /**
     * Возвращает тенор как строковое значение.
     */
    public String value() {
        return name();
    }
}
