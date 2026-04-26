package com.alligator.market.domain.instrument.asset.forex.fxspot.codec;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;

import java.util.Objects;

/**
 * Утилита содержит методы формирования кода и символа инструмента FOREX_SPOT,
 * а также метод разложения строкового кода инструмента FOREX_SPOT на составные компоненты.
 */
public final class FxSpotCodec {

    /* Префикс кода инструмента: класс актива + продукт (FOREX_SPOT). */
    private static final String TYPE_PREFIX = "FOREX_SPOT_";

    /* Разделитель между парой и тенором. */
    private static final char SEP = '_';

    /* Приватный конструктор: запрещаем создание экземпляров класса-утилиты. */
    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class instantiation is not allowed");
    }

    /**
     * Формирует строковый символ инструмента.
     *
     * @param baseCode  Код базовой валюты
     * @param quoteCode Код котируемой валюты
     * @param tenor     Тенор даты валютирования
     * @return Символ инструмента
     */
    public static InstrumentSymbol fxSpotSymbol(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentSymbol.of(baseCode.value() + quoteCode.value() + SEP + tenor.name());
    }

    /**
     * Формирует внутренний код инструмента.
     *
     * @param baseCode  Код базовой валюты
     * @param quoteCode Код котируемой валюты
     * @param tenor     Тенор даты валютирования
     * @return Код инструмента
     */
    public static InstrumentCode fxSpotCode(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentCode.of(TYPE_PREFIX + fxSpotSymbol(baseCode, quoteCode, tenor).value());
    }
}
