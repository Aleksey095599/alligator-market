package com.alligator.market.domain.process.quotemonitor.capturer;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

import java.time.Duration;

public final class LiveQuoteMonitorCapturer implements MarketDataCapturer {
    public static final CapturerCode CAPTURER_CODE =
            CapturerCode.of("LIVE_QUOTE_MONITOR");

    public static final CapturerDisplayName DISPLAY_NAME =
            CapturerDisplayName.of("Live Quote Monitor");

    public static final CapturerPassport PASSPORT =
            new CapturerPassport(DISPLAY_NAME);

    public static final Duration UPDATE_INTERVAL = Duration.ofSeconds(1);

    public static final LiveQuoteMonitorCapturerPolicy POLICY =
            new LiveQuoteMonitorCapturerPolicy(UPDATE_INTERVAL);

    @Override
    public CapturerCode capturerCode() {
        return CAPTURER_CODE;
    }

    @Override
    public CapturerPassport passport() {
        return PASSPORT;
    }

    @Override
    public LiveQuoteMonitorCapturerPolicy policy() {
        return POLICY;
    }
}
