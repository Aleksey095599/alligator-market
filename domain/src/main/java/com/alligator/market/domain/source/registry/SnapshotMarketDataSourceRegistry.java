package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.passport.vo.MarketDataSourceDisplayName;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.*;

public final class SnapshotMarketDataSourceRegistry implements MarketDataSourceRegistry {

    private final Map<MarketDataSourceCode, MarketDataSource> sourcesByCode;

    private final Map<MarketDataSourceCode, MarketDataSourcePassport> passportsByCode;

    public SnapshotMarketDataSourceRegistry(List<? extends MarketDataSource> sources) {
        Objects.requireNonNull(sources, "sources must not be null");

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("Market data source registry must contain at least one source");
        }

        Map<MarketDataSourceCode, MarketDataSource> sourcesMap = new LinkedHashMap<>();
        Map<MarketDataSourceCode, MarketDataSourcePassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataSource source : sources) {
            Objects.requireNonNull(source, "source must not be null");

            MarketDataSourceCode code = Objects.requireNonNull(source.sourceCode(),
                    "source.sourceCode must not be null");

            MarketDataSourcePassport passport = Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            Objects.requireNonNull(source.policy(),
                    "source.policy must not be null");

            MarketDataSource prev = sourcesMap.put(code, source);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Duplicate market data source code detected (sourceCode=" + code.value() + ")"
                );
            }

            MarketDataSourceDisplayName displayName = Objects.requireNonNull(passport.displayName(),
                    "source.passport.displayName must not be null");

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate market data source display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.sourcesByCode = Collections.unmodifiableMap(sourcesMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<MarketDataSourceCode, MarketDataSource> sourcesByCode() {
        return sourcesByCode;
    }

    @Override
    public Map<MarketDataSourceCode, MarketDataSourcePassport> passportsByCode() {
        return passportsByCode;
    }
}
