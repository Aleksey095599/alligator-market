package com.alligator.market.domain.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.MarketDataCaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessDisplayName;

import java.time.Duration;
import java.util.Objects;

/**
 * Процесс захвата рыночных данных FX Spot для аналитического TWAP по последней цене.
 */
public final class AnalyticalFxSpotTwapLastPrice implements MarketDataCaptureProcess {

    public static final MarketDataCaptureProcessCode PROCESS_CODE =
            MarketDataCaptureProcessCode.of("ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE");

    public static final MarketDataCaptureProcessDisplayName DISPLAY_NAME =
            MarketDataCaptureProcessDisplayName.of("Analytical FX Spot TWAP by last price");

    public static final MarketDataCaptureProcessPassport PASSPORT =
            new MarketDataCaptureProcessPassport(DISPLAY_NAME);

    public static final Policy POLICY =
            new Policy(Duration.ofSeconds(1));

    @Override
    public MarketDataCaptureProcessCode processCode() {
        return PROCESS_CODE;
    }

    @Override
    public MarketDataCaptureProcessPassport passport() {
        return PASSPORT;
    }

    @Override
    public Policy policy() {
        return POLICY;
    }

    /**
     * Политика аналитического FX Spot TWAP-процесса по последней цене.
     *
     * @param captureInterval фактический интервал захвата тиков
     */
    public record Policy(
            Duration captureInterval
    ) implements MarketDataCaptureProcessPolicy {

        public Policy {
            Objects.requireNonNull(captureInterval, "captureInterval must not be null");

            if (captureInterval.isZero() || captureInterval.isNegative()) {
                throw new IllegalArgumentException("captureInterval must be positive");
            }
        }
    }
}
