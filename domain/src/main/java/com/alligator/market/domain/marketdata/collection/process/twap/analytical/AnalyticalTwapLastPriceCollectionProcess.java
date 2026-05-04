package com.alligator.market.domain.marketdata.collection.process.twap.analytical;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.collection.process.MarketDataCollectionProcess;
import com.alligator.market.domain.marketdata.collection.process.passport.CollectionProcessPassport;
import com.alligator.market.domain.marketdata.collection.process.policy.CollectionProcessPolicy;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessDisplayName;

import java.time.Duration;
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

    public static final CollectionProcessPassport PASSPORT =
            new CollectionProcessPassport(DISPLAY_NAME);

    public static final AnalyticalTwapLastPriceCollectionProcessPolicy POLICY =
            new AnalyticalTwapLastPriceCollectionProcessPolicy(Duration.ofSeconds(1));

    private final Set<InstrumentCode> supportedInstrumentCodes;

    public AnalyticalTwapLastPriceCollectionProcess(Set<InstrumentCode> supportedInstrumentCodes) {
        this.supportedInstrumentCodes = copySupportedInstrumentCodes(supportedInstrumentCodes);
    }

    @Override
    public MarketDataCollectionProcessCode processCode() {
        return PROCESS_CODE;
    }

    @Override
    public CollectionProcessPassport passport() {
        return PASSPORT;
    }

    @Override
    public CollectionProcessPolicy policy() {
        return POLICY;
    }

    @Override
    public Set<InstrumentCode> supportedInstrumentCodes() {
        return supportedInstrumentCodes;
    }

    private static Set<InstrumentCode> copySupportedInstrumentCodes(Set<InstrumentCode> raw) {
        Objects.requireNonNull(raw, "supportedInstrumentCodes must not be null");

        if (raw.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        for (InstrumentCode instrumentCode : raw) {
            Objects.requireNonNull(instrumentCode, "supportedInstrumentCodes must not contain null");
        }

        return Set.copyOf(raw);
    }
}
