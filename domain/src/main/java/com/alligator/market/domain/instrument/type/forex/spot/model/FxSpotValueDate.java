package com.alligator.market.domain.instrument.type.forex.spot.model;

/**
 * Коды дат валютирования инструмента FX_SPOT.
 */
public enum FxSpotValueDate {
    /* ↓↓ Константы. */
    TOD("TOD"),
    TOM("TOM"),
    SPOT("SPOT");

    /* Поле enum. */
    private final String code; // Строковая переменная

    /** Конструктор enum (всегда неявно private). */
    FxSpotValueDate(String code) {
        this.code = code;
    }

    /** Возвращает строковый код. */
    public String code() {
        return code;
    }

    /** Ищет значение enum по строковому коду. */
    public static FxSpotValueDate fromCode(String code) {
        for (FxSpotValueDate value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported FX spot value date code: " + code);
    }
}
