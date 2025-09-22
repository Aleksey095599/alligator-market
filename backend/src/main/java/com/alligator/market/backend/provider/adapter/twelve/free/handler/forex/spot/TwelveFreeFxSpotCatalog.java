package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

import java.util.Set;

/**
 * Каталог поддерживаемых инструментов FX Spot провайдера TwelveData (free).
 */
public final class TwelveFreeFxSpotCatalog {

    /** Скрываем конструктор. */
    private TwelveFreeFxSpotCatalog() {
    }

    /* ↓↓ Валюты. */
    private static final Currency EUR = new Currency("EUR", "Euro", "European Union", 2);
    private static final Currency USD = new Currency("USD", "United States Dollar", "United States", 2);

    /* ↓↓ Инструменты. */
    private static final FxSpot EUR_USD = new FxSpot(EUR, USD, ValueDateCode.TOM, 4);

    /** Набор поддерживаемых инструментов провайдера. */
    public static final Set<FxSpot> SUPPORTED = Set.of(EUR_USD);
}
