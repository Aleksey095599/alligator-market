package com.alligator.market.domain.instrument.type.forex.spot.codec;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotTenor;

import java.util.Objects;

/**
 * Утилита содержит методы формирования кода и символа инструмента FX_SPOT, а также
 * метод разложения строкового кода инструмента FX_SPOT на составные компоненты.
 */
public final class FxSpotCodec {

    /* Префикс кода инструмента: отображает тип FX_SPOT. */
    private static final String TYPE_PREFIX = InstrumentType.FX_SPOT.code() + "_";

    /* Разделитель между парой и тенором. */
    private static final char SEP = '_';

    /* Длина буквенного кода валюты и валютной пары. */
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = 2 * CURRENCY_CODE_LENGTH;

    /**
     * Приватный конструктор: запрещаем создание экземпляров.
     */
    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Формирует символ инструмента из кодов двух валют {@link CurrencyCode} и
     * тенора даты валютирования {@link FxSpotTenor}.
     */
    public static String fxSpotSymbol(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return baseCode.value() + quoteCode.value() + SEP + tenor.value();
    }

    /**
     * Формирует внутренний код инструмента из {@link CurrencyCode} и тенора даты валютирования {@link FxSpotTenor}.
     *
     * <p>Добавляем префикс {@code TYPE_PREFIX} к символу инструмента.</p>
     */
    public static InstrumentCode fxSpotCode(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentCode.of(TYPE_PREFIX + fxSpotSymbol(baseCode, quoteCode, tenor));
    }

    /**
     * Простая перегрузка:
     * Формирует внутренний код инструмента из {@link Currency} и тенора даты валютирования {@link FxSpotTenor}.
     */
    public static InstrumentCode fxSpotCode(Currency baseCurrency, Currency quoteCurrency, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCurrency, "baseCurrency must not be null");
        Objects.requireNonNull(quoteCurrency, "quoteCurrency must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return fxSpotCode(baseCurrency.code(), quoteCurrency.code(), tenor);
    }

    /**
     * Разбирает объект-значение кода инструмента {@link InstrumentCode} на
     * составные компоненты {@link FxSpotCodeParts}.
     */
    public static FxSpotCodeParts parseFxSpotCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        String codeValue = instrumentCode.value();

        if (!codeValue.startsWith(TYPE_PREFIX)) {
            throw new IllegalArgumentException("Instrument code must start with " + TYPE_PREFIX);
        }

        final int start = TYPE_PREFIX.length();
        final int sep = codeValue.indexOf(SEP, start);
        if (sep < 0) {
            throw new IllegalArgumentException("Instrument code must contain separator '_' after prefix "
                    + TYPE_PREFIX);
        }

        final String pair = codeValue.substring(start, sep);
        if (pair.length() != CURRENCY_PAIR_LENGTH) {
            throw new IllegalArgumentException("Currency pair must contain " + CURRENCY_PAIR_LENGTH + " characters");
        }

        // Разделение валютной пары на валюты
        final String baseRaw = pair.substring(0, CURRENCY_CODE_LENGTH);
        final String quoteRaw = pair.substring(CURRENCY_CODE_LENGTH);

        final String tenorRaw = codeValue.substring(sep + 1);
        if (tenorRaw.isEmpty()) {
            throw new IllegalArgumentException("Tenor must not be empty");
        }

        // Валидация кодов валют
        final CurrencyCode baseCode = CurrencyCode.of(baseRaw);
        final CurrencyCode quoteCode = CurrencyCode.of(quoteRaw);

        // Валидация тенора даты валютирования
        final FxSpotTenor tenor = FxSpotTenor.fromValue(tenorRaw);

        return new FxSpotCodeParts(baseCode, quoteCode, tenor);
    }

    /**
     * Разбирает строковый код инструмента формата {@code FX_SPOT_<AAA><BBB>_<FxSpotTenor>} на составные компоненты.
     */
    public static FxSpotCodeParts parseFxSpotCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "Instrument code must not be null");

        return parseFxSpotCode(InstrumentCode.of(instrumentCode));
    }

    /**
     * Модель разложения кода инструмента на валюты и тенор даты валютирования.
     */
    public record FxSpotCodeParts(
            CurrencyCode baseCode,
            CurrencyCode quoteCode,
            FxSpotTenor tenor
    ) {
    }
}
