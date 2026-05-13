package com.alligator.market.domain.capturer.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Map;

public interface RuntimeCapturerRegistry {

    Map<CapturerCode, MarketDataCapturer> capturersByCode();
}
