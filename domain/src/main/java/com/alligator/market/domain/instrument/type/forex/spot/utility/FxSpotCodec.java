package com.alligator.market.domain.instrument.type.forex.spot.utility;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

import java.util.Objects;

/**
 * Утилита задаёт правила кодирования и декодирования инструмента FX_SPOT.
 */
public final class FxSpotCodec {

    private static final String TYPE_PREFIX = InstrumentType.FX_SPOT + "_";
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = CURRENCY_CODE_LENGTH * 2;

    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** Формирует символ инструмента FX_SPOT. */
    public static String fxSpotSymbol(String base, String quote, ValueDateCode valueDate) {
        Objects.requireNonNull(base, "Base currency code must not be null");
        Objects.requireNonNull(quote, "Quote currency code must not be null");
        Objects.requireNonNull(valueDate, "Value date code must not be null");
        return base.toUpperCase() + quote.toUpperCase() + "_" + valueDate.name();
    }

    /** Формирует код инструмента FX_SPOT из параметров доменной модели. */
    public static String fxSpotCode(String base, String quote, ValueDateCode valueDate) {
        Objects.requireNonNull(base, "Base currency code must not be null");
        Objects.requireNonNull(quote, "Quote currency code must not be null");
        Objects.requireNonNull(valueDate, "Value date code must not be null");
        return TYPE_PREFIX + fxSpotSymbol(base, quote, valueDate);
    }

    /**
     * Разбирает код инструмента FX_SPOT на параметры доменной модели.
     * Обратная функция {@link #fxSpotCode(String, String, ValueDateCode)}.
     */
    public static FxSpotCodeParts parseFxSpotCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "Instrument code must not be null");

        if (!instrumentCode.startsWith(TYPE_PREFIX)) {
            throw new IllegalArgumentException("Instrument code must start with " + TYPE_PREFIX);
        }

        // Отбрасываем префикс типа инструмента и делим строку на символ и дату валютирования
        String[] parts = instrumentCode.substring(TYPE_PREFIX.length()).split("_");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Instrument code must contain currency pair and value date");
        }

        String symbol = parts[0];
        if (symbol.length() != CURRENCY_PAIR_LENGTH) {
            throw new IllegalArgumentException("Currency pair must contain " + CURRENCY_PAIR_LENGTH + " characters");
        }

        ValueDateCode valueDateCode = parseValueDate(parts[1]);

        // Разделяем символ на коды валют
        String base = symbol.substring(0, CURRENCY_CODE_LENGTH);
        String quote = symbol.substring(CURRENCY_CODE_LENGTH, CURRENCY_PAIR_LENGTH);

        return new FxSpotCodeParts(base, quote, valueDateCode);
    }

    private static ValueDateCode parseValueDate(String valueDate) {
        try {
            return ValueDateCode.valueOf(valueDate);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported value date code: " + valueDate, ex);
        }
    }
}
