package com.alligator.market.domain.process.quotemonitor.capturer;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

public final class QuoteMonitorCapturer implements MarketDataCapturer {
    public static final CapturerCode CAPTURER_CODE =
            CapturerCode.of("QUOTE_MONITOR");

    public static final CapturerDisplayName DISPLAY_NAME =
            CapturerDisplayName.of("Quote Monitor");

    public static final CapturerPassport PASSPORT =
            new CapturerPassport(DISPLAY_NAME);

    public static final QuoteMonitorCapturerPolicy POLICY =
            new QuoteMonitorCapturerPolicy();

    @Override
    public CapturerCode capturerCode() {
        return CAPTURER_CODE;
    }

    @Override
    public CapturerPassport passport() {
        return PASSPORT;
    }

    @Override
    public QuoteMonitorCapturerPolicy policy() {
        return POLICY;
    }
}
