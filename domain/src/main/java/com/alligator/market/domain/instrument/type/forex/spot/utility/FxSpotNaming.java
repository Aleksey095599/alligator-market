package com.alligator.market.domain.instrument.type.forex.spot.utility;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

import java.util.Objects;

/**
 * Утилита задает правила генерации кода и символа для инструмента FX_SPOT.
 */
public final class FxSpotNaming {

    private FxSpotNaming() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** Формирует код инструмента FX_SPOT. */
    public static String fxSpotCode(String base, String quote, ValueDateCode valueDate) {
        Objects.requireNonNull(base, "Base currency code must not be null");
        Objects.requireNonNull(quote, "Quote currency code must not be null");
        Objects.requireNonNull(valueDate, "Value date code must not be null");
        return InstrumentType.FX_SPOT + "_" + base.toUpperCase() + quote.toUpperCase() + "_" + valueDate.name();
    }

    /** Формирует символ инструмента FX_SPOT. */
    public static String fxSpotSymbol(String base, String quote, ValueDateCode valueDate) {
        Objects.requireNonNull(base, "Base currency code must not be null");
        Objects.requireNonNull(quote, "Quote currency code must not be null");
        Objects.requireNonNull(valueDate, "Value date code must not be null");
        return base.toUpperCase() + quote.toUpperCase() + "_" + valueDate.name();
    }
}
