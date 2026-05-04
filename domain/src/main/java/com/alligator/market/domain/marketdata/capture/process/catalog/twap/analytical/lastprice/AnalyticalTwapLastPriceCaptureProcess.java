package com.alligator.market.domain.marketdata.capture.process.catalog.twap.analytical.lastprice;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.CaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessDisplayName;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;

/**
 * Процесс фиксации рыночных данных для аналитического TWAP на основе тиков последней цены.
 */
public final class AnalyticalTwapLastPriceCaptureProcess implements MarketDataCaptureProcess {

    public static final CaptureProcessCode PROCESS_CODE =
            CaptureProcessCode.of("ANALYTICAL_TWAP_LAST_PRICE");

    public static final CaptureProcessDisplayName DISPLAY_NAME =
            CaptureProcessDisplayName.of("Analytical TWAP by last price");

    public static final CaptureProcessPassport PASSPORT =
            new CaptureProcessPassport(DISPLAY_NAME);

    public static final AnalyticalTwapLastPriceCaptureProcessPolicy POLICY =
            new AnalyticalTwapLastPriceCaptureProcessPolicy(Duration.ofSeconds(1));

    private final Set<InstrumentCode> supportedInstrumentCodes;

    public AnalyticalTwapLastPriceCaptureProcess(Set<InstrumentCode> supportedInstrumentCodes) {
        this.supportedInstrumentCodes = copySupportedInstrumentCodes(supportedInstrumentCodes);
    }

    @Override
    public CaptureProcessCode processCode() {
        return PROCESS_CODE;
    }

    @Override
    public CaptureProcessPassport passport() {
        return PASSPORT;
    }

    @Override
    public CaptureProcessPolicy policy() {
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
