package com.alligator.market.domain.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.MDCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.MDCaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessDisplayName;

import java.time.Duration;
import java.util.Objects;

/**
 * Процесс фиксации рыночных данных FX Spot для аналитического TWAP по последней цене.
 */
public final class AnalyticalFxSpotTwapLastPrice implements MDCaptureProcess {

    public static final MDCaptureProcessCode PROCESS_CODE =
            MDCaptureProcessCode.of("ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE");

    public static final MDCaptureProcessDisplayName DISPLAY_NAME =
            MDCaptureProcessDisplayName.of("Analytical FX Spot TWAP by last price");

    public static final MDCaptureProcessPassport PASSPORT =
            new MDCaptureProcessPassport(DISPLAY_NAME);

    public static final Policy POLICY =
            new Policy(Duration.ofSeconds(1));

    @Override
    public MDCaptureProcessCode processCode() {
        return PROCESS_CODE;
    }

    @Override
    public MDCaptureProcessPassport passport() {
        return PASSPORT;
    }

    @Override
    public Policy policy() {
        return POLICY;
    }

    /**
     * Политика аналитического FX Spot TWAP-процесса по последней цене.
     *
     * @param captureInterval фактический интервал фиксации тиков
     */
    public record Policy(
            Duration captureInterval
    ) implements MDCaptureProcessPolicy {

        public Policy {
            Objects.requireNonNull(captureInterval, "captureInterval must not be null");

            if (captureInterval.isZero() || captureInterval.isNegative()) {
                throw new IllegalArgumentException("captureInterval must be positive");
            }
        }
    }
}
