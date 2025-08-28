package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;

/**
 * Фабрика сборки модели FX_SPOT.
 */
public final class FxSpotFactory {

    private FxSpotFactory() {}

    /** Собирает модель по валютам. */
    public static FxSpot fromCurrencies(Currency base,
                                        Currency quote,
                                        ValueDateCode valueDateCode,
                                        Integer quoteDecimal) {
        return new FxSpot(base, quote, valueDateCode, quoteDecimal);
    }

    /** Собирает модель по кодам валют. */
    public static FxSpot fromCurrencyCodes(String baseCode,
                                           String quoteCode,
                                           ValueDateCode valueDateCode,
                                           Integer quoteDecimal) {
        Currency base = new Currency(baseCode, null, null, null);
        Currency quote = new Currency(quoteCode, null, null, null);
        return fromCurrencies(base, quote, valueDateCode, quoteDecimal);
    }

    /** Собирает модель по коду инструмента FX_SPOT. */
    public static FxSpot fromInstrumentCode(String code,
                                            Integer quoteDecimal) {
        String base = code.substring(0, 3);
        String quote = code.substring(3, 6);
        ValueDateCode valueDate = ValueDateCode.valueOf(code.substring(7));
        return fromCurrencyCodes(base, quote, valueDate, quoteDecimal);
    }
}

