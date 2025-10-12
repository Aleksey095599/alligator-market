package com.alligator.market.domain.instrument.type.forex.spot.codec;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;

import java.util.Objects;

/**
 * Утилита содержит методы формирования атрибутов {@code code} и {@code symbol} инструмента FX_SPOT и
 * метод разложения строкового кода {@code code} инструмента FX_SPOT на составные компоненты.
 */
public final class FxSpotCodec {

    /* Префикс типа в code. Ожидается, что InstrumentType.FX_SPOT.code() возвращает "FX_SPOT". */
    private static final String TYPE_PREFIX = InstrumentType.FX_SPOT.code() + "_";

    /* Разделитель между парой и кодом даты. */
    private static final char SEP = '_';

    /* Длина буквенного кода валюты и валютной пары. */
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = 2 * CURRENCY_CODE_LENGTH;

    /** Приватный конструктор. */
    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class"); // Запрещаем создание экземпляров
    }

    /** Формирует символ инструмента из доменных моделей кодов валют и даты валютирования. */
    public static String fxSpotSymbol(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotValueDate valueDate) {
        Objects.requireNonNull(baseCode, "Base currency code must not be null");
        Objects.requireNonNull(quoteCode, "Quote currency code must not be null");
        Objects.requireNonNull(valueDate, "Value date must not be null");
        return baseCode.value() + quoteCode.value() + SEP + valueDate.code();
    }

    /* ↪ Перегрузка для случая доменных моделей валют. */
    public static String fxSpotSymbol(Currency base, Currency quote, FxSpotValueDate valueDate) {
        Objects.requireNonNull(base, "Base currency must not be null");
        Objects.requireNonNull(quote, "Quote currency must not be null");
        return fxSpotSymbol(base.code(), quote.code(), valueDate);
    }

    /** Формирует внутренний код инструмента из доменных моделей кодов валют и даты валютирования. */
    public static String fxSpotCode(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotValueDate valueDate) {
        // Добавляем префикс к символу
        return TYPE_PREFIX + fxSpotSymbol(baseCode, quoteCode, valueDate);
    }

    /* ↪ Перегрузка для случая доменных моделей валют. */
    public static String fxSpotCode(Currency base, Currency quote, FxSpotValueDate valueDate) {
        Objects.requireNonNull(base, "Base currency must not be null");
        Objects.requireNonNull(quote, "Quote currency must not be null");
        return fxSpotCode(base.code(), quote.code(), valueDate);
    }

    /**
     * Разбирает строковый код инструмента формата {@code FX_SPOT_<AAA><BBB>_<FxSpotValueDate>} на составные компоненты.
     */
    public static FxSpotCodeParts parseFxSpotCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "Instrument code must not be null");

        if (!instrumentCode.startsWith(TYPE_PREFIX)) {
            throw new IllegalArgumentException("Instrument code must start with " + TYPE_PREFIX);
        }

        final int start = TYPE_PREFIX.length();
        final int sep = instrumentCode.indexOf(SEP, start);
        if (sep < 0) {
            throw new IllegalArgumentException("Instrument code must contain separator '_' after prefix "
                    + TYPE_PREFIX);
        }

        final String pair = instrumentCode.substring(start, sep);
        if (pair.length() != CURRENCY_PAIR_LENGTH) {
            throw new IllegalArgumentException("Currency pair must contain " + CURRENCY_PAIR_LENGTH + " characters");
        }

        // Разделение валютной пары на валюты
        final String baseRaw  = pair.substring(0, CURRENCY_CODE_LENGTH);
        final String quoteRaw = pair.substring(CURRENCY_CODE_LENGTH);

        final String valueDateRaw = instrumentCode.substring(sep + 1);
        if (valueDateRaw.isEmpty()) {
            throw new IllegalArgumentException("Value date code must not be empty");
        }

        // Валидация кодов валют
        final CurrencyCode baseCode  = CurrencyCode.of(baseRaw);
        final CurrencyCode quoteCode = CurrencyCode.of(quoteRaw);

        // Валидация даты валютирования
        final FxSpotValueDate valueDate = FxSpotValueDate.fromCode(valueDateRaw);

        return new FxSpotCodeParts(baseCode, quoteCode, valueDate);
    }

    /** Модель разложения кода инструмента на валюты и дату валютирования. */
    public record FxSpotCodeParts(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotValueDate valueDate) { }
}
