package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.contract.AbstractInstrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;

import java.util.Objects;

/**
 * Модель финансового инструмента FX_SPOT.
 */
public class FxSpot extends AbstractInstrument {
    /* ↓↓ Базовые атрибуты инструмента. */
    private final Currency base;
    private final Currency quote;
    private final ValueDateCode valueDateCode;
    private final int defaultQuoteFractionDigits;

    /**
     * Конструктор инструмента с проверками.
     *
     * @throws FxSpotSameCurrenciesException если валюты совпадают
     * @throws NullPointerException          если параметр не передан
     * @throws IllegalArgumentException      если код валюты пустой
     */
    public FxSpot(Currency base, Currency quote, ValueDateCode valueDateCode, int defaultQuoteFractionDigits) {

        // ↓↓ Базовые проверки аргументов
        this.base = Objects.requireNonNull(base, "base must not be null");
        this.quote = Objects.requireNonNull(quote, "quote must not be null");
        this.valueDateCode = Objects.requireNonNull(valueDateCode, "valueDateCode must not be null");
        this.defaultQuoteFractionDigits = defaultQuoteFractionDigits;

        // Ограничение на стандартное количество знаков после запятой в котировке
        if (this.defaultQuoteFractionDigits < 0 || this.defaultQuoteFractionDigits > 10) {
            throw new IllegalArgumentException("defaultQuoteFractionDigits must be between 0 and 10");
        }
        // Валюты должны отличиться
        if (this.base.code().equals(this.quote.code())) {
            throw new FxSpotSameCurrenciesException();
        }
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
    public ValueDateCode valueDateCode() {
        return valueDateCode;
    }

    /** Количество знаков после запятой в котировке (согласно рыночной практике). */
    public int defaultQuoteFractionDigits() {
        return defaultQuoteFractionDigits;
    }

    /** Внутренний код инструмента (уникален в контексте приложения). */
    @Override
    public String code() {
        return FxSpotCodec.fxSpotCode(base, quote, valueDateCode);
    }

    /** Символ инструмента для отображения в UI. */
    @Override
    public String symbol() {
        return FxSpotCodec.fxSpotSymbol(base, quote, valueDateCode);
    }

    /** Тип инструмента. */
    @Override
    public InstrumentType type() {
        return InstrumentType.FX_SPOT;
    }
}
