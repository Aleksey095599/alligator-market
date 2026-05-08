package com.alligator.market.domain.marketdata.capturer.registry;

import com.alligator.market.domain.marketdata.capturer.MarketDataCapturer;
import com.alligator.market.domain.marketdata.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registry of market data capturers available in the application.
 */
public interface MarketDataCapturerRegistry {

    /**
     * Immutable map of capturer code to capturer.
     */
    Map<MarketDataCapturerCode, MarketDataCapturer> capturersByCode();

    /**
     * Derived immutable map of capturer code to passport.
     */
    default Map<MarketDataCapturerCode, MarketDataCapturerPassport> passportsByCode() {
        Map<MarketDataCapturerCode, MarketDataCapturerPassport> map = new LinkedHashMap<>();

        for (Map.Entry<MarketDataCapturerCode, MarketDataCapturer> entry : capturersByCode().entrySet()) {
            MarketDataCapturerCode code = entry.getKey();
            MarketDataCapturer capturer = entry.getValue();

            MarketDataCapturerPassport passport = Objects.requireNonNull(capturer.passport(),
                    "capturer.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
