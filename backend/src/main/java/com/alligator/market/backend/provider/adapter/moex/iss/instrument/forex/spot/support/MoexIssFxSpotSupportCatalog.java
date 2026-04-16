package com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.support;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.tenor.FxSpotTenor;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;

import java.util.*;

/**
 * Каталог FOREX_SPOT-инструментов, поддерживаемых провайдером MOEX ISS.
 *
 * <p>Содержит инструменты и соответствия "код инструмента в приложении ↔ SECID MOEX ISS".</p>
 */
public class MoexIssFxSpotSupportCatalog {

    /* Доменные валюты. */
    private static final Currency USD = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
    private static final Currency RUB = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
    private static final Currency CNY = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);

    /* Доменные инструменты. */
    private static final FxSpot USD_RUB = new FxSpot(USD, RUB, FxSpotTenor.TOM, 4);
    private static final FxSpot CNY_RUB = new FxSpot(CNY, RUB, FxSpotTenor.TOM, 4);

    /* Карта соответствий: код инструмента ↔ SECID. */
    private static final Map<InstrumentCode, String> DOMAIN_CODE_TO_SECID;

    /**
     * Набор кодов поддерживаемых доменных инструментов.
     */
    public static final Set<InstrumentCode> SUPPORTED_DOMAIN_CODES;

    static {
        // Строим карту соответствий доменных кодов и SECID
        Map<InstrumentCode, String> map = new LinkedHashMap<>();

        map.put(USD_RUB.instrumentCode(), "USD000UTSTOM");
        map.put(CNY_RUB.instrumentCode(), "CNYRUB_TOM");

        DOMAIN_CODE_TO_SECID = Collections.unmodifiableMap(map);
        SUPPORTED_DOMAIN_CODES = Collections.unmodifiableSet(
                new LinkedHashSet<>(DOMAIN_CODE_TO_SECID.keySet())
        );
    }

    /**
     * Конвертер доменного кода инструмента в SECID MOEX ISS.
     *
     * <p>Конвертер опирается на карту соответствий доменных кодов и SECID MOEX ISS {@link #DOMAIN_CODE_TO_SECID}.</p>
     *
     * @param instrumentCode доменный код инструмента
     * @return SECID MOEX ISS
     */
    public static String moexSecidOf(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Ищем значение из карты соответствий
        String secid = DOMAIN_CODE_TO_SECID.get(instrumentCode);
        if (secid == null) {
            throw new IllegalStateException(
                    "Missing MOEX ISS SECID mapping for supported instrumentCode: " + instrumentCode.value()
            );
        }
        return secid;
    }

    /* Скрываем конструктор. */
    private MoexIssFxSpotSupportCatalog() {
        throw new UnsupportedOperationException("Utility class");
    }
}
