package com.alligator.market.domain.capturer.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Map;

public interface CapturerRegistry {

    Map<CapturerCode, MarketDataCapturer> capturersByCode();

    Map<CapturerCode, CapturerPassport> passportsByCode();
}
