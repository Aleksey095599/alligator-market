package com.alligator.market.domain.marketdata.collection.process.twap.analytical;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.collection.process.MarketDataCollectionProcess;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessDisplayName;

import java.util.Objects;
import java.util.Set;

/**
 * Процесс сбора рыночных данных для аналитического TWAP на основе тиков последней цены.
 */
public final class AnalyticalTwapLastPriceCollectionProcess implements MarketDataCollectionProcess {

    public static final MarketDataCollectionProcessCode PROCESS_CODE =
            MarketDataCollectionProcessCode.of("ANALYTICAL_TWAP_LAST_PRICE");

    public static final MarketDataCollectionProcessDisplayName DISPLAY_NAME =
            MarketDataCollectionProcessDisplayName.of("Analytical TWAP by last price");

    private final Set<InstrumentCode> supportedInstrumentCodes;

    public AnalyticalTwapLastPriceCollectionProcess(Set<InstrumentCode> supportedInstrumentCodes) {
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        this.supportedInstrumentCodes = Set.copyOf(supportedInstrumentCodes);
    }

    @Override
    public MarketDataCollectionProcessCode processCode() {
        return PROCESS_CODE;
    }

    @Override
    public MarketDataCollectionProcessDisplayName displayName() {
        return DISPLAY_NAME;
    }

    @Override
    public Set<InstrumentCode> supportedInstrumentCodes() {
        return supportedInstrumentCodes;
    }
}
