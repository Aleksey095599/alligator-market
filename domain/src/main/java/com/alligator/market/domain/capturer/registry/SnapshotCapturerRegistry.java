package com.alligator.market.domain.capturer.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SnapshotCapturerRegistry implements CapturerRegistry {

    private final Map<CapturerCode, MarketDataCapturer> capturersByCode;

    public SnapshotCapturerRegistry(List<? extends MarketDataCapturer> capturers) {
        Objects.requireNonNull(capturers, "capturers must not be null");

        if (capturers.isEmpty()) {
            throw new IllegalArgumentException("Market data capturer registry must contain at least one capturer");
        }

        Map<CapturerCode, MarketDataCapturer> capturersMap = new LinkedHashMap<>();

        for (MarketDataCapturer capturer : capturers) {
            Objects.requireNonNull(capturer, "capturer must not be null");

            CapturerCode code = Objects.requireNonNull(capturer.capturerCode(),
                    "capturer.capturerCode must not be null");

            Objects.requireNonNull(capturer.passport(),
                    "capturer.passport must not be null");

            MarketDataCapturer previous = capturersMap.put(code, capturer);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate market data capturer code detected (code=" + code.value() + ")"
                );
            }
        }

        this.capturersByCode = Collections.unmodifiableMap(capturersMap);
    }

    @Override
    public Map<CapturerCode, MarketDataCapturer> capturersByCode() {
        return capturersByCode;
    }
}
