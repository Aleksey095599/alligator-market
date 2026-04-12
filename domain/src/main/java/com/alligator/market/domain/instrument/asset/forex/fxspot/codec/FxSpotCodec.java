package com.alligator.market.domain.instrument.asset.forex.fxspot.codec;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.model.FxSpotTenor;

import java.util.Objects;

/**
 * Утилита содержит методы формирования кода и символа инструмента FOREX_SPOT,
 * а также метод разложения строкового кода инструмента FOREX_SPOT на составные компоненты.
 */
public final class FxSpotCodec {

    /* Префикс кода инструмента: класс актива + тип контракта (FOREX_SPOT). */
    private static final String TYPE_PREFIX = "FOREX_SPOT_";

    /* Разделитель между парой и тенором. */
    private static final char SEP = '_';

    /* Длина буквенного кода валюты и валютной пары. */
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = 2 * CURRENCY_CODE_LENGTH;

    /*
     * Приватный конструктор: запрещаем создание экземпляров класса-утилиты.
     */
    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class instantiation is not allowed");
    }

    /**
     * Формирует строковый символ инструмента из кодов валют и тенора даты валютирования.
     */
    public static InstrumentSymbol fxSpotSymbol(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentSymbol.of(baseCode.value() + quoteCode.value() + SEP + tenor.name());
    }

    /**
     * Формирует внутренний код инструмента из кодов валют и тенора даты валютирования.
     */
    public static InstrumentCode fxSpotCode(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentCode.of(TYPE_PREFIX + fxSpotSymbol(baseCode, quoteCode, tenor).value());
    }

    /**
     * Разбирает объект-значение кода инструмента на составные компоненты {@see FxSpotCodeParts}.
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
        final FxSpotTenor tenor = FxSpotTenor.valueOf(tenorRaw);

        return new FxSpotCodeParts(baseCode, quoteCode, tenor);
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
