package com.alligator.market.domain.provider.contract.descriptor;

/**
 * Список возможных режимов доставки данных провайдерами рыночных данных.
 */
public enum DeliveryMode {
    /* ↓↓ Константы. */
    PUSH("PUSH"), // Провайдер активно отправляет данные при их появлении
    PULL("PULL"); // Клиент периодически запрашивает актуальные данные

    /* Поле enum. */
    private final String code; // Строковое представление значения

    /* ↓↓ Конструктор enum (всегда неявно private). */
    DeliveryMode(String code) {
        this.code = code;
    }

    /** Возвращает строковый код. */
    public String code() {
        return code;
    }

    /** Ищет значение enum по строковому коду. */
    public static DeliveryMode fromCode(String code) {
        for (DeliveryMode value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported delivery mode: " + code);
    }
}
