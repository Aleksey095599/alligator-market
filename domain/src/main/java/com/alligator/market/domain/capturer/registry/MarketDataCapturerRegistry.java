package com.alligator.market.domain.capturer.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public interface MarketDataCapturerRegistry {

    Map<MarketDataCapturerCode, MarketDataCapturer> capturersByCode();

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
