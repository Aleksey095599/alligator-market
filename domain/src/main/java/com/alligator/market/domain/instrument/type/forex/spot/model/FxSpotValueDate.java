package com.alligator.market.domain.instrument.type.forex.spot.model;

import java.util.Locale;
import java.util.Objects;

/**
 * Коды дат валютирования инструмента FX_SPOT.
 */
public enum FxSpotValueDate {
    /* Константы. */
    TOD, TOM, SPOT;

    /** Возвращает строковый код (совпадает с именем константы). */
    public String code() {
        return name();
    }

    /** Парсит строковый код в enum (игнорирует регистр и пробелы). */
    public static FxSpotValueDate fromCode(String code) {
        Objects.requireNonNull(code, "code must not be null");
        try {
            return FxSpotValueDate.valueOf(code.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported FX_SPOT value date code: " + code, ex);
        }
    }
}
