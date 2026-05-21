package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.support;

import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class MoexIssFxSpotSupportCatalog {
    private static final Currency USD = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
    private static final Currency RUB = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
    private static final Currency CNY = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);

    private static final FxSpot USD_RUB = new FxSpot(USD, RUB, FxSpotTenor.TOM, 4);
    private static final FxSpot CNY_RUB = new FxSpot(CNY, RUB, FxSpotTenor.TOM, 4);

    private static final Map<InstrumentCode, SourceInstrumentCode> DOMAIN_CODE_TO_SECID;

    public static final Set<FxSpot> SUPPORTED_INSTRUMENTS;

    static {
        SUPPORTED_INSTRUMENTS = Collections.unmodifiableSet(
                new LinkedHashSet<>(List.of(USD_RUB, CNY_RUB))
        );

        Map<InstrumentCode, SourceInstrumentCode> map = new LinkedHashMap<>();

        map.put(USD_RUB.instrumentCode(), SourceInstrumentCode.of("USD000UTSTOM"));
        map.put(CNY_RUB.instrumentCode(), SourceInstrumentCode.of("CNYRUB_TOM"));

        DOMAIN_CODE_TO_SECID = Collections.unmodifiableMap(map);
    }

    private MoexIssFxSpotSupportCatalog() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SourceInstrumentCode moexSecidOf(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        SourceInstrumentCode secid = DOMAIN_CODE_TO_SECID.get(instrumentCode);
        if (secid == null) {
            throw new IllegalStateException(
                    "Missing MOEX ISS SECID mapping for supported instrumentCode: " + instrumentCode.value()
            );
        }
        return secid;
    }
}
