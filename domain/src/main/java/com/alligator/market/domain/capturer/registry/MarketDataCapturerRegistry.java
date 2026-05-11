package com.alligator.market.domain.capturer.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Map;

public interface MarketDataCapturerRegistry {

    Map<MarketDataCapturerCode, MarketDataCapturer> capturersByCode();

    Map<MarketDataCapturerCode, MarketDataCapturerPassport> passportsByCode();
}
