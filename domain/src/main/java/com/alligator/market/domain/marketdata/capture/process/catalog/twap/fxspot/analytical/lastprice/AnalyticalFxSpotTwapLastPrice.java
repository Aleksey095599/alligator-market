package com.alligator.market.domain.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.CaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessDisplayName;

import java.time.Duration;
import java.util.Objects;

/**
 * Процесс фиксации рыночных данных FX Spot для аналитического TWAP по последней цене.
 */
public final class AnalyticalFxSpotTwapLastPrice implements MarketDataCaptureProcess {

    public static final CaptureProcessCode PROCESS_CODE =
            CaptureProcessCode.of("ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE");

    public static final CaptureProcessDisplayName DISPLAY_NAME =
            CaptureProcessDisplayName.of("Analytical FX Spot TWAP by last price");

    public static final CaptureProcessPassport PASSPORT =
            new CaptureProcessPassport(DISPLAY_NAME);

    public static final Policy POLICY =
            new Policy(Duration.ofSeconds(1));

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

    /**
     * Политика аналитического FX Spot TWAP-процесса по последней цене.
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
