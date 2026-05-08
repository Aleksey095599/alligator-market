package com.alligator.market.domain.capturer;

import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.policy.MarketDataCapturerPolicy;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

public interface MarketDataCapturer {

    MarketDataCapturerCode capturerCode();

    MarketDataCapturerPassport passport();

    MarketDataCapturerPolicy policy();
}
