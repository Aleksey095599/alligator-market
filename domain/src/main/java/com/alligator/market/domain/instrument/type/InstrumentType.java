package com.alligator.market.domain.instrument.type;

/**
 * Типы финансовых инструментов.
 */
public enum InstrumentType {
    /* ↓↓ Константы. */
    FX_SPOT("FX_SPOT"),
    FX_SWAP("FX_SWAP");

    /* Поле enum. */
    private final String code; // Строковое представление значения

    /** Конструктор enum (всегда неявно private). */
    InstrumentType(String code) {
        this.code = code;
    }

    /** Возвращает строковый код. */
    public String code() {
        return code;
    }

    /** Ищет значение enum по строковому коду. */
    public static InstrumentType fromCode(String code) {
        for (InstrumentType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported instrument type: " + code);
    }
}
