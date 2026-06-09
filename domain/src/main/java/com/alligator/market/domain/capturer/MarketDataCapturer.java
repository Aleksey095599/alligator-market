package com.alligator.market.domain.capturer;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

public interface MarketDataCapturer {

    CapturerCode capturerCode();

    CapturerPassport passport();
}
