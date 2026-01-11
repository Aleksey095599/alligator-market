package com.alligator.market.domain.provider.contract.passport;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Коды поддерживаемых методов доступа провайдеров к рыночным данным.
 */
public enum AccessMethod {
    /* Константы: коды методов доступа (далее – коды). */
    API_POLL,     // <-- Метод периодического опроса API провайдера для получения рыночных данных
    WEBSOCKET,    // <-- Метод получения данных через WebSocket соединение в режиме реального времени
    FIX_PROTOCOL; // <-- Метод доступа через FIX протокол для высокочастотной торговли

    /* Коды в виде списка и единой строки (для сообщений об ошибках). */
    private static final List<String> SUPPORTED_CODES = Arrays.stream(values()).map(Enum::name).toList();
    private static final String SUPPORTED_CODES_JOINED = String.join(", ", SUPPORTED_CODES);

    /**
     * Парсит код (trim + upper-case). В ошибке подсказывает допустимые значения.
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
                    "Unsupported AccessMethod code: '" + code + "'. Supported: " + SUPPORTED_CODES_JOINED, ex);
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
    @SuppressWarnings("unused")
    public String code() {
        return name();
    }
}
