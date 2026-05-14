package com.alligator.market.domain.capturer.passport.registry.runtime;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.registry.RuntimeCapturerRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class RuntimeCapturerPassportRegistryAdapter implements RuntimeCapturerPassportRegistry {
    private final RuntimeCapturerRegistry runtimeCapturerRegistry;

    public RuntimeCapturerPassportRegistryAdapter(RuntimeCapturerRegistry runtimeCapturerRegistry) {
        this.runtimeCapturerRegistry = Objects.requireNonNull(
                runtimeCapturerRegistry,
                "runtimeCapturerRegistry must not be null"
        );
    }

    @Override
    public Map<CapturerCode, CapturerPassport> passportsByCode() {
        Map<CapturerCode, MarketDataCapturer> capturersByCode = Objects.requireNonNull(
                runtimeCapturerRegistry.capturersByCode(),
                "capturersByCode must not be null"
        );

        Map<CapturerCode, CapturerPassport> passportsByCode = new LinkedHashMap<>(capturersByCode.size());
        Set<String> displayNamesLower = new HashSet<>();

        for (Map.Entry<CapturerCode, MarketDataCapturer> entry : capturersByCode.entrySet()) {
            CapturerCode code = Objects.requireNonNull(entry.getKey(), "capturersByCode must not contain null keys");
            MarketDataCapturer capturer = Objects.requireNonNull(
                    entry.getValue(),
                    "capturersByCode must not contain null values"
            );

            CapturerPassport passport = Objects.requireNonNull(
                    capturer.passport(),
                    "capturer.passport must not be null"
            );
            CapturerDisplayName displayName = Objects.requireNonNull(
                    passport.displayName(),
                    "capturer.passport.displayName must not be null"
            );

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate market data capturer display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsByCode.put(code, passport);
        }

        return Collections.unmodifiableMap(passportsByCode);
    }
}
