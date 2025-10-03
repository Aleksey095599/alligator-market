package com.alligator.market.domain.instrument.utility;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

import java.util.Objects;

/**
 * Утилита для генерации кодов инструментов.
 */
public final class InstrumentCodeFactory {

    private InstrumentCodeFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** Формирует код инструмента FX_SPOT. */
    public static String fxSpotCode(String baseCurrencyCode, String quoteCurrencyCode, ValueDateCode valueDateCode) {
        Objects.requireNonNull(baseCurrencyCode, "Base currency code must not be null");
        Objects.requireNonNull(quoteCurrencyCode, "Quote currency code must not be null");
        Objects.requireNonNull(valueDateCode, "Value date code must not be null");
        return baseCurrencyCode + quoteCurrencyCode + "_" + valueDateCode.name();
    }
}
