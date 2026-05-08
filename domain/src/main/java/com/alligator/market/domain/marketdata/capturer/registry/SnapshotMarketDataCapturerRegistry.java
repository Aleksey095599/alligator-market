package com.alligator.market.domain.marketdata.capturer.registry;

import com.alligator.market.domain.marketdata.capturer.MarketDataCapturer;
import com.alligator.market.domain.marketdata.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable snapshot implementation of {@link MarketDataCapturerRegistry}.
 */
public final class SnapshotMarketDataCapturerRegistry implements MarketDataCapturerRegistry {

    private final Map<MarketDataCapturerCode, MarketDataCapturer> capturersByCode;
    private final Map<MarketDataCapturerCode, MarketDataCapturerPassport> passportsByCode;

    public SnapshotMarketDataCapturerRegistry(List<? extends MarketDataCapturer> capturers) {
        Objects.requireNonNull(capturers, "capturers must not be null");

        if (capturers.isEmpty()) {
            throw new IllegalArgumentException("Market data capturer registry must contain at least one capturer");
        }

        Map<MarketDataCapturerCode, MarketDataCapturer> capturersMap = new LinkedHashMap<>();
        Map<MarketDataCapturerCode, MarketDataCapturerPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataCapturer capturer : capturers) {
            Objects.requireNonNull(capturer, "capturer must not be null");

            MarketDataCapturerCode code = Objects.requireNonNull(capturer.capturerCode(),
                    "capturer.capturerCode must not be null");

            MarketDataCapturerPassport passport = Objects.requireNonNull(capturer.passport(),
                    "capturer.passport must not be null");

            Objects.requireNonNull(capturer.policy(),
                    "capturer.policy must not be null");

            MarketDataCapturer previous = capturersMap.put(code, capturer);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate market data capturer code detected (code=" + code.value() + ")"
                );
            }

            MarketDataCapturerDisplayName displayName = Objects.requireNonNull(passport.displayName(),
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
    public Map<MarketDataCapturerCode, MarketDataCapturer> capturersByCode() {
        return capturersByCode;
    }

    @Override
    public Map<MarketDataCapturerCode, MarketDataCapturerPassport> passportsByCode() {
        return passportsByCode;
    }
}
