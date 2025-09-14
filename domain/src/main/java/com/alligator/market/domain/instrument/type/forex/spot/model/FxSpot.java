package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.base.AbstractInstrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;

/**
 * Модель финансового инструмента FX_SPOT.
 */
public class FxSpot extends AbstractInstrument {

    private final Currency base;
    private final Currency quote;
    private final ValueDateCode valueDateCode;
    private final Integer quoteDecimal;

    /**
     * Конструктор инструмента.
     *
     * @throws FxSpotSameCurrenciesException если валюты совпадают
     */
    public FxSpot(Currency base, Currency quote, ValueDateCode valueDateCode, Integer quoteDecimal) {
        // Проверяем, что валюты отличаются
        if (base.code().equals(quote.code())) {
            throw new FxSpotSameCurrenciesException();
        }
        this.base = base;
        this.quote = quote;
        this.valueDateCode = valueDateCode;
        this.quoteDecimal = quoteDecimal;
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
