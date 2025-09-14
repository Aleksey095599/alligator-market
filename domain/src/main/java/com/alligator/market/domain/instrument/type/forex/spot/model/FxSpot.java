package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.base.AbstractInstrument;
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
    private final Integer quoteDecimal;

    /**
     * Конструктор инструмента.
     *
     * @throws FxSpotSameCurrenciesException если валюты совпадают
     * @throws NullPointerException          если параметр не передан
     * @throws IllegalArgumentException      если код валюты пустой
     */
    public FxSpot(Currency base, Currency quote, ValueDateCode valueDateCode, Integer quoteDecimal) {
        // → Проверяем входные параметры
        this.base = Objects.requireNonNull(base, "Base currency is required");
        this.quote = Objects.requireNonNull(quote, "Quote currency is required");
        this.valueDateCode = Objects.requireNonNull(valueDateCode, "Value date code is required");
        this.quoteDecimal = Objects.requireNonNull(quoteDecimal, "Quote decimal is required");

        // → Проверяем, что коды валют заполнены
        if (this.base.code().isBlank()) {
            throw new IllegalArgumentException("Base currency code must not be blank");
        }
        if (this.quote.code().isBlank()) {
            throw new IllegalArgumentException("Quote currency code must not be blank");
        }

        // → Проверяем, что валюты отличаются
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

    /** Количество знаков в котировке. */
    public Integer quoteDecimal() {
        return quoteDecimal;
    }

    @Override
    public String code() {
        return base.code() + quote.code() + "_" + valueDateCode;
    }

    @Override
    public InstrumentType type() {
        return InstrumentType.FX_SPOT;
    }
}
