package com.alligator.market.domain.instrument.type.forex.spot.codec;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

import java.util.Objects;

/**
 * Утилита для форматирования и парсинга атрибутов code и symbol инструмента FX_SPOT.
 */
public final class FxSpotCodec {

    private static final String TYPE_PREFIX = InstrumentType.FX_SPOT.code() + "_";
    private static final char SEP = '_';
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = 6;

    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** Формирует символ FX_SPOT из атрибутов доменной модели. */
    public static String fxSpotSymbol(Currency baseCurrency, Currency quoteCurrency, ValueDateCode valueDate) {
        Objects.requireNonNull(baseCurrency, "Base currency must not be null");
        Objects.requireNonNull(quoteCurrency, "Quote currency must not be null");
        Objects.requireNonNull(valueDate, "Value date must not be null");
        return baseCurrency.code() + quoteCurrency.code() + SEP + valueDate.code();
    }

    /** Формирует внутренний код FX_SPOT из атрибутов доменной модели. */
    public static String fxSpotCode(Currency base, Currency quote, ValueDateCode valueDate) {
        return TYPE_PREFIX + fxSpotSymbol(base, quote, valueDate);
    }

    /**
     * Разбирает внутренний код FX_SPOT на строковые составные части.
     */
    public static FxSpotCodeParts parseFxSpotCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "Instrument code must not be null");

        if (!instrumentCode.startsWith(TYPE_PREFIX)) {
            throw new IllegalArgumentException("Instrument code must start with " + TYPE_PREFIX);
        }

        final int start = TYPE_PREFIX.length();
        final int sep = instrumentCode.indexOf(SEP, start);
        if (sep < 0) {
            throw new IllegalArgumentException("Instrument code must contain separator '_'");
        }

        final String pair = instrumentCode.substring(start, sep);
        if (pair.length() != CURRENCY_PAIR_LENGTH) {
            throw new IllegalArgumentException("Currency pair must contain " + CURRENCY_PAIR_LENGTH + " characters");
        }

        // Разделяем на AAA и BBB (дополнительных проверок не делаем — это задача Currency)
        final String baseCode = pair.substring(0, CURRENCY_CODE_LENGTH);
        final String quoteCode = pair.substring(CURRENCY_CODE_LENGTH);
        final String valueDateRaw = instrumentCode.substring(sep + 1);
        final ValueDateCode valueDate = ValueDateCode.fromCode(valueDateRaw);

        return new FxSpotCodeParts(baseCode, quoteCode, valueDate);
    }

    /** Разложение кода FX_SPOT на строковые составляющие (без построения модели Currency). */
    public record FxSpotCodeParts(String baseCode, String quoteCode, ValueDateCode valueDate) { }
}
