package com.alligator.market.domain.process.quotemonitor;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.policy.MarketDataCapturerPolicy;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerDisplayName;

import java.time.Duration;

public final class LiveQuoteMonitorCapturer implements MarketDataCapturer {
    public static final MarketDataCapturerCode CAPTURER_CODE =
            MarketDataCapturerCode.of("LIVE_QUOTE_MONITOR");

    public static final MarketDataCapturerDisplayName DISPLAY_NAME =
            MarketDataCapturerDisplayName.of("Live Quote Monitor");

    public static final MarketDataCapturerPassport PASSPORT =
            new MarketDataCapturerPassport(DISPLAY_NAME);

    public static final Duration UPDATE_INTERVAL = Duration.ofSeconds(1);

    public static final MarketDataCapturerPolicy POLICY =
            new MarketDataCapturerPolicy(UPDATE_INTERVAL);

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
