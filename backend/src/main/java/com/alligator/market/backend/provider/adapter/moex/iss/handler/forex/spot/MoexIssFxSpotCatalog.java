package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;

import java.util.Set;

/**
 * Каталог поддерживаемых инструментов FX_SPOT провайдера MOEX ISS.
 */
public class MoexIssFxSpotCatalog {

    /* Валюты. */
    private static final Currency USD = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
    private static final Currency RUB = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
    private static final Currency CNY = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);

    /* Инструменты. */
    private static final FxSpot USD_RUB = new FxSpot(USD, RUB, FxSpotValueDate.TOM, 4);
    private static final FxSpot CNY_RUB = new FxSpot(CNY, RUB, FxSpotValueDate.TOM, 4);

    /**
     * Набор поддерживаемых кодов инструментов.
     */
    public static final Set<String> SUPPORTED_CODES = Set.of(USD_RUB.instrumentCode(), CNY_RUB.instrumentCode());

    /**
     * Скрываем конструктор.
     */
    private MoexIssFxSpotCatalog() {
    }
}
