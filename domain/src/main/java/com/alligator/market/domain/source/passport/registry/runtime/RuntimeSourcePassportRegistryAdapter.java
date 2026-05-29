package com.alligator.market.domain.source.passport.registry.runtime;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class RuntimeSourcePassportRegistryAdapter implements RuntimeSourcePassportRegistry {
    private final RuntimeSourceRegistry runtimeSourceRegistry;

    public RuntimeSourcePassportRegistryAdapter(RuntimeSourceRegistry runtimeSourceRegistry) {
        this.runtimeSourceRegistry = Objects.requireNonNull(
                runtimeSourceRegistry,
                "runtimeSourceRegistry must not be null"
        );
    }

    @Override
    public Map<SourceCode, SourcePassport> passportsByCode() {
        Map<SourceCode, MarketDataSource> sourcesByCode = Objects.requireNonNull(
                runtimeSourceRegistry.sourcesByCode(),
                "sourcesByCode must not be null"
        );

        Map<SourceCode, SourcePassport> passportsByCode = new LinkedHashMap<>(sourcesByCode.size());
        Set<String> displayNamesLower = new HashSet<>();

        for (Map.Entry<SourceCode, MarketDataSource> entry : sourcesByCode.entrySet()) {
            SourceCode code = Objects.requireNonNull(entry.getKey(), "sourcesByCode must not contain null keys");
            MarketDataSource source = Objects.requireNonNull(
                    entry.getValue(),
                    "sourcesByCode must not contain null values"
            );

            SourcePassport passport = Objects.requireNonNull(
                    source.passport(),
                    "source.passport must not be null"
            );
            SourceDisplayName displayName = Objects.requireNonNull(
                    passport.displayName(),
                    "source.passport.displayName must not be null"
            );

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate market data source display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsByCode.put(code, passport);
        }

        return Collections.unmodifiableMap(passportsByCode);
    }
}
