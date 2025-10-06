package com.alligator.market.domain.provider.contract.descriptor;

/**
 * Список методов доступа к рыночным данным.
 */
public enum AccessMethod {
    /* ↓↓ Константы. */
    API_POLL("API_POLL"),    // Метод периодического опроса API провайдера для получения рыночных данных
    WEBSOCKET("WEBSOCKET"),  // Метод получения данных через WebSocket соединение в режиме реального времени
    FIX_PROTOCOL("FIX_PROTOCOL"); // Метод доступа через FIX протокол для высокочастотной торговли

    /* Поле enum. */
    private final String code; // Строковое представление значения

    /* ↓↓ Конструктор enum (всегда неявно private). */
    AccessMethod(String code) {
        this.code = code;
    }

    /** Возвращает строковый код. */
    public String code() {
        return code;
    }

    /** Ищет значение enum по строковому коду. */
    public static AccessMethod fromCode(String code) {
        for (AccessMethod value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported access method: " + code);
    }
}
