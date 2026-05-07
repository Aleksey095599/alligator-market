package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.MarketDataCaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessDisplayName;

import java.time.Duration;
import java.util.Objects;

/**
 * Backend-описание процесса захвата FX Spot last price для аналитического TWAP.
 *
 * <p>Класс регистрирует идентичность процесса и техническую capture policy. Расчетные правила,
 * когда они появятся, должны жить в отдельных доменных классах.</p>
 */
public final class AnalyticalFxSpotTwapLastPriceCaptureProcess implements MarketDataCaptureProcess {

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
     * Техническая capture policy для этого backend-процесса.
     *
     * @param captureInterval интервал между попытками захвата
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
