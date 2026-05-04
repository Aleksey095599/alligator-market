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
 * Процесс фиксации рыночных данных для аналитического TWAP по последней цене.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class AnalyticalTwapLastPrice implements MarketDataCaptureProcess {

    public static final CaptureProcessCode PROCESS_CODE =
            CaptureProcessCode.of("ANALYTICAL_TWAP_LAST_PRICE");

    public static final CaptureProcessDisplayName DISPLAY_NAME =
            CaptureProcessDisplayName.of("Analytical TWAP by last price");

    public static final CaptureProcessPassport PASSPORT =
            new CaptureProcessPassport(DISPLAY_NAME);

    public static final Policy POLICY =
            new Policy(Duration.ofSeconds(1));

    private final Set<InstrumentCode> supportedInstrumentCodes;

    public AnalyticalTwapLastPrice(Set<InstrumentCode> supportedInstrumentCodes) {
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
    public Policy policy() {
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

    /**
     * Политика аналитического TWAP-процесса по последней цене.
     *
     * @param captureInterval фактический интервал фиксации тиков
     */
    public record Policy(
            Duration captureInterval
    ) implements CaptureProcessPolicy {

        public Policy {
            Objects.requireNonNull(captureInterval, "captureInterval must not be null");

            if (captureInterval.isZero() || captureInterval.isNegative()) {
                throw new IllegalArgumentException("captureInterval must be positive");
            }
        }
    }
}
