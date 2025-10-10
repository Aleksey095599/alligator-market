package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.contract.AbstractInstrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;

import java.util.Objects;

/**
 * Модель финансового инструмента FX_SPOT.
 */
public class FxSpot extends AbstractInstrument {
    /* ↓↓ Базовые атрибуты инструмента. */
    private final Currency base;
    private final Currency quote;
    private final FxSpotValueDate valueDate;
    private final int defaultQuoteFractionDigits;

    /** Конструктор с проверками. */
    public FxSpot(Currency base, Currency quote, FxSpotValueDate valueDate, int defaultQuoteFractionDigits) {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(base, "base must not be null");
        Objects.requireNonNull(quote, "quote must not be null");
        Objects.requireNonNull(valueDate, "valueDateCode must not be null");

        // Ограничение на количество знаков после запятой в котировке согласно рыночной практике
        if (defaultQuoteFractionDigits < 0 || defaultQuoteFractionDigits > 10) {
            throw new IllegalArgumentException("defaultQuoteFractionDigits must be between 0 and 10");
        }

        // Валюты не должны совпадать
        if (base.code().equals(quote.code())) {
            throw new FxSpotSameCurrenciesException(base.code(), quote.code());
        }

        this.base = base;
        this.quote = quote;
        this.valueDate = valueDate;
        this.defaultQuoteFractionDigits = defaultQuoteFractionDigits;
    }

    /** Базовая валюта. */
    public Currency base() {
        return base;
    }

    /** Котируемая валюта. */
    public Currency quote() {
        return quote;
    }

    /** Код даты валютирования. */
    public FxSpotValueDate valueDate() {
        return valueDate;
    }

    /** Количество знаков после запятой в котировке согласно рыночной практике (по умолчанию). */
    public int defaultQuoteFractionDigits() {
        return defaultQuoteFractionDigits;
    }

    /** Внутренний код инструмента (уникален в контексте приложения). */
    @Override
    public String code() {
        return FxSpotCodec.fxSpotCode(base, quote, valueDate);
    }

    /** Символ инструмента для отображения в UI. */
    @Override
    public String symbol() {
        return FxSpotCodec.fxSpotSymbol(base, quote, valueDate);
    }

    /** Тип инструмента. */
    @Override
    public InstrumentType type() {
        return InstrumentType.FX_SPOT;
    }
}
