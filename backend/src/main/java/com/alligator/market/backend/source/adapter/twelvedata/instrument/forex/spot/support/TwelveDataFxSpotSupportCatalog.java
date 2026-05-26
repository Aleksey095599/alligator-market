package com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.support;

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

public final class TwelveDataFxSpotSupportCatalog {
    private static final Currency INR = new Currency(CurrencyCode.of("INR"), "Indian Rupee", "India", 2);
    private static final Currency USD = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);

    private static final FxSpot INR_USD = new FxSpot(INR, USD, FxSpotTenor.SPOT, 4);

    private static final Map<InstrumentCode, SourceInstrumentCode> DOMAIN_CODE_TO_SOURCE_CODE;
    private static final Map<InstrumentCode, String> DOMAIN_CODE_TO_TWELVE_SYMBOL;

    public static final Set<FxSpot> SUPPORTED_INSTRUMENTS;

    static {
        SUPPORTED_INSTRUMENTS = Collections.unmodifiableSet(
                new LinkedHashSet<>(List.of(INR_USD))
        );

        Map<InstrumentCode, SourceInstrumentCode> sourceCodeMap = new LinkedHashMap<>();
        sourceCodeMap.put(INR_USD.instrumentCode(), SourceInstrumentCode.of("INRUSD"));
        DOMAIN_CODE_TO_SOURCE_CODE = Collections.unmodifiableMap(sourceCodeMap);

        Map<InstrumentCode, String> twelveSymbolMap = new LinkedHashMap<>();
        twelveSymbolMap.put(INR_USD.instrumentCode(), "INR/USD");
        DOMAIN_CODE_TO_TWELVE_SYMBOL = Collections.unmodifiableMap(twelveSymbolMap);
    }

    private TwelveDataFxSpotSupportCatalog() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SourceInstrumentCode sourceInstrumentCodeOf(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        SourceInstrumentCode sourceInstrumentCode = DOMAIN_CODE_TO_SOURCE_CODE.get(instrumentCode);
        if (sourceInstrumentCode == null) {
            throw new IllegalStateException(
                    "Missing Twelve Data source instrument code mapping for supported instrumentCode: " +
                            instrumentCode.value()
            );
        }
        return sourceInstrumentCode;
    }

    public static String twelveSymbolOf(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        String twelveSymbol = DOMAIN_CODE_TO_TWELVE_SYMBOL.get(instrumentCode);
        if (twelveSymbol == null) {
            throw new IllegalStateException(
                    "Missing Twelve Data symbol mapping for supported instrumentCode: " + instrumentCode.value()
            );
        }
        return twelveSymbol;
    }
}
