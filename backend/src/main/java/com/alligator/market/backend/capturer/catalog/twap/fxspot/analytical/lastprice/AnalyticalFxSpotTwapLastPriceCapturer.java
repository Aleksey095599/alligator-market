package com.alligator.market.backend.capturer.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.policy.MarketDataCapturerPolicy;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerDisplayName;

import java.time.Duration;
import java.util.Objects;

/**
 * Backend-описание процесса захвата FX Spot last price для аналитического TWAP.
 *
 * <p>Класс регистрирует идентичность процесса и техническую capture policy. Расчетные правила,
 * когда они появятся, должны жить в отдельных доменных классах.</p>
 */
public final class AnalyticalFxSpotTwapLastPriceCapturer implements MarketDataCapturer {

    public static final MarketDataCapturerCode CAPTURER_CODE =
            MarketDataCapturerCode.of("ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE");

    public static final MarketDataCapturerDisplayName DISPLAY_NAME =
            MarketDataCapturerDisplayName.of("Analytical FX Spot TWAP by last price");

    public static final MarketDataCapturerPassport PASSPORT =
            new MarketDataCapturerPassport(DISPLAY_NAME);

    public static final Policy POLICY =
            new Policy(Duration.ofSeconds(1));

    @Override
    public MarketDataCapturerCode capturerCode() {
        return CAPTURER_CODE;
    }

    @Override
    public MarketDataCapturerPassport passport() {
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
    ) implements MarketDataCapturerPolicy {

        public Policy {
            Objects.requireNonNull(captureInterval, "captureInterval must not be null");

            if (captureInterval.isZero() || captureInterval.isNegative()) {
                throw new IllegalArgumentException("captureInterval must be positive");
            }
        }
    }
}
