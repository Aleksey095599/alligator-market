package com.alligator.market.domain.provider.model.passport;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Режимы доставки данных провайдерами рыночных данных.
 */
public enum DeliveryMode {
    /* Поддерживаемые режимы доставки (константы). */
    PUSH, // <-- Провайдер активно отправляет данные при их появлении
    PULL; // <-- Клиент периодически запрашивает актуальные данные

    /* Список всех режимов доставки и представление этого списка в виде единой строки (иногда полезно). */
    private static final List<String> SUPPORTED_CODES = Arrays.stream(values()).map(Enum::name).toList();
    private static final String SUPPORTED_CODES_JOINED = String.join(", ", SUPPORTED_CODES);

    /**
     * Парсит режим доставки (trim + upper-case). В ошибке подсказывает допустимые значения.
     */
    @SuppressWarnings("unused")
    public static DeliveryMode fromCode(String code) {
        Objects.requireNonNull(code, "DeliveryMode code must not be null");

        // Обрезаем пробелы
        String trimmed = code.strip();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("DeliveryMode code is blank");
        }

        // Делаем нормализацию регистра
        String normalized = trimmed.toUpperCase(Locale.ROOT);

        // Парсинг
        try {
            return DeliveryMode.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unsupported DeliveryMode code: '" + code + "'. Supported: " + SUPPORTED_CODES_JOINED, ex);
        }
    }

    /**
     * Возвращает список поддерживаемых режимов доставки (для валидации/документации).
     */
    @SuppressWarnings("unused")
    public static List<String> supportedCodes() {
        return SUPPORTED_CODES;
    }

    /**
     * Возвращает режим доставки как строковое значение.
     */
    @SuppressWarnings("unused")
    public String code() {
        return name();
    }
}
