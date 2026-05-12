package com.alligator.market.domain.capturer.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotCapturerRegistry implements CapturerRegistry {

    private final Map<CapturerCode, MarketDataCapturer> capturersByCode;
    private final Map<CapturerCode, CapturerPassport> passportsByCode;

    public SnapshotCapturerRegistry(List<? extends MarketDataCapturer> capturers) {
        Objects.requireNonNull(capturers, "capturers must not be null");

        if (capturers.isEmpty()) {
            throw new IllegalArgumentException("Market data capturer registry must contain at least one capturer");
        }

        Map<CapturerCode, MarketDataCapturer> capturersMap = new LinkedHashMap<>();
        Map<CapturerCode, CapturerPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataCapturer capturer : capturers) {
            Objects.requireNonNull(capturer, "capturer must not be null");

            CapturerCode code = Objects.requireNonNull(capturer.capturerCode(),
                    "capturer.capturerCode must not be null");

            CapturerPassport passport = Objects.requireNonNull(capturer.passport(),
                    "capturer.passport must not be null");

            Objects.requireNonNull(capturer.policy(),
                    "capturer.policy must not be null");

            MarketDataCapturer previous = capturersMap.put(code, capturer);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate market data capturer code detected (code=" + code.value() + ")"
                );
            }

            CapturerDisplayName displayName = Objects.requireNonNull(passport.displayName(),
                    "capturer.passport.displayName must not be null");

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate market data capturer display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.capturersByCode = Collections.unmodifiableMap(capturersMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<CapturerCode, MarketDataCapturer> capturersByCode() {
        return capturersByCode;
    }

    @Override
    public Map<CapturerCode, CapturerPassport> passportsByCode() {
        return passportsByCode;
    }
}
