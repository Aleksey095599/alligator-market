package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;

import java.util.*;

/**
 * Каталог FX_SPOT-инструментов, поддерживаемых провайдером MOEX ISS.
 *
 * <p>Содержит доменные инструменты и соответствие "доменный код <--> SECID MOEX ISS".
 */
public class MoexIssFxSpotInstruments {

    /* Доменные валюты. */
    private static final Currency USD = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
    private static final Currency RUB = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
    private static final Currency CNY = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);

    /* Доменные инструменты. */
    private static final FxSpot USD_RUB = new FxSpot(USD, RUB, FxSpotValueDate.TOM, 4);
    private static final FxSpot CNY_RUB = new FxSpot(CNY, RUB, FxSpotValueDate.TOM, 4);

    /* Карта соответствий: доменный код <--> SECID. */
    private static final Map<String, String> DOMAIN_CODE_TO_SECID;

    /**
     * Набор кодов поддерживаемых доменных инструментов.
     */
    public static final Set<String> SUPPORTED_DOMAIN_CODES;

    static {
        // Строим карту соответствий доменных кодов и SECID
        Map<String, String> map = new LinkedHashMap<>();

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
     * <p>Конвертер максимально прост и опирается на следующие инварианты:
     * <ul>
     *   <li>Карта соответствий строится на основе надёжных доменных моделей;</li>
     *   <li>Обработчик {@link MoexIssFxSpotHandler} перед вызовом конвертера гарантирует,
     *       что {@code instrumentCode} принадлежит {@link #SUPPORTED_DOMAIN_CODES}.</li>
     * </ul>
     */
    public static String moexSecidOf(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Ищем значение из карты соответствий
        String secid = DOMAIN_CODE_TO_SECID.get(instrumentCode);
        if (secid == null) {
            throw new IllegalStateException(
                    "Missing MOEX ISS SECID mapping for supported instrumentCode: " + instrumentCode
            );
        }
        return secid;
    }

    /* Скрываем конструктор. */
    private MoexIssFxSpotInstruments() {
    }
}
