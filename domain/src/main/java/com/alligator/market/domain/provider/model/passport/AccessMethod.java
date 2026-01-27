package com.alligator.market.domain.provider.model.passport;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Методы доступа провайдеров к рыночным данным.
 */
public enum AccessMethod {
    /* Поддерживаемые методы доступа (константы). */
    API_POLL,     // <-- Метод периодического опроса API провайдера для получения рыночных данных
    WEBSOCKET,    // <-- Метод получения данных через WebSocket соединение в режиме реального времени
    FIX_PROTOCOL; // <-- Метод доступа через FIX протокол для высокочастотной торговли

    /* Список всех методов и представление этого списка в виде единой строки (иногда полезно). */
    private static final List<String> ACCESS_METHODS = Arrays.stream(values()).map(Enum::name).toList();
    private static final String ACCESS_METHODS_JOINED = String.join(", ", ACCESS_METHODS);

    /**
     * Парсит метод (trim + upper-case). В ошибке подсказывает допустимые значения.
     */
    @SuppressWarnings("unused")
    public static AccessMethod fromCode(String code) {
        Objects.requireNonNull(code, "AccessMethod code must not be null");

        // Обрезаем пробелы
        String trimmed = code.strip();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("AccessMethod code is blank");
        }

        // Делаем нормализацию регистра
        String normalized = trimmed.toUpperCase(Locale.ROOT);

        // Парсинг
        try {
            return AccessMethod.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unsupported AccessMethod code: '" + code + "'. Supported: " + ACCESS_METHODS_JOINED, ex);
        }
    }

    /**
     * Возвращает список заданных в приложении методов (для валидации/документации).
     */
    @SuppressWarnings("unused")
    public static List<String> supportedCodes() {
        return ACCESS_METHODS;
    }

    /**
     * Возвращает метод как строковое значение.
     */
    @SuppressWarnings("unused")
    public String code() {
        return name();
    }
}
