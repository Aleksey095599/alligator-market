package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot;

import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotTenor;

import java.util.Set;

/**
 * Каталог поддерживаемых инструментов FX Spot провайдера TwelveData (free).
 */
public final class TwelveFreeFxSpotCatalog {

    /* Валюты. */
    private static final Currency EUR = new Currency(CurrencyCode.of("EUR"), "Euro", "European Union", 2);
    private static final Currency USD = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);

    /* Инструменты. */
    private static final FxSpot EUR_USD = new FxSpot(EUR, USD, FxSpotTenor.TOM, 4);

    /**
     * Набор поддерживаемых кодов инструментов.
     */
    public static final Set<String> SUPPORTED_CODES = Set.of(EUR_USD.instrumentCode());

    /**
     * Скрываем конструктор.
     */
    private TwelveFreeFxSpotCatalog() {
    }
}
