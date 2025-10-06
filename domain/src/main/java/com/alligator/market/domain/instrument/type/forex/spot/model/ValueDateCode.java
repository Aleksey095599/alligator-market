package com.alligator.market.domain.instrument.type.forex.spot.model;

/**
 * Коды дат валютирования инструмента FX_SPOT.
 */
public enum ValueDateCode {
    /* ↓↓ Константы. */
    TOD("TOD"),
    TOM("TOM"),
    SPOT("SPOT");

    /* Поле enum. */
    private final String code; // Строковая переменная

    /** Конструктор enum (всегда неявно private). */
    ValueDateCode(String code) {
        this.code = code;
    }

    /** Возвращает строковый код. */
    public String code() {
        return code;
    }

    /** Ищет значение enum по строковому коду. */
    public static ValueDateCode fromCode(String code) {
        for (ValueDateCode value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported value date code: " + code);
    }
}
