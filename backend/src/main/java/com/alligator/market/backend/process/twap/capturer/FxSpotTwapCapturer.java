package com.alligator.market.backend.process.twap.capturer;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.policy.MarketDataCapturerPolicy;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerDisplayName;

import java.time.Duration;

public final class FxSpotTwapCapturer implements MarketDataCapturer {
    public static final MarketDataCapturerCode CAPTURER_CODE =
            MarketDataCapturerCode.of("ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE");

    public static final MarketDataCapturerDisplayName DISPLAY_NAME =
            MarketDataCapturerDisplayName.of("Analytical FX Spot TWAP by last price");

    public static final MarketDataCapturerPassport PASSPORT =
            new MarketDataCapturerPassport(DISPLAY_NAME);

    public static final MarketDataCapturerPolicy POLICY =
            new MarketDataCapturerPolicy(Duration.ofSeconds(1));

    @Override
    public MarketDataCapturerCode capturerCode() {
        return CAPTURER_CODE;
    }

    @Override
    public MarketDataCapturerPassport passport() {
        return PASSPORT;
    }

    @Override
    public MarketDataCapturerPolicy policy() {
        return POLICY;
    }
}
