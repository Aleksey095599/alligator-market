package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.*;

public final class SnapshotSourceRegistry implements SourceRegistry {

    private final Map<SourceCode, MarketSource> sourcesByCode;

    private final Map<SourceCode, SourcePassport> passportsByCode;

    public SnapshotSourceRegistry(List<? extends MarketSource> sources) {
        Objects.requireNonNull(sources, "sources must not be null");

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("Market source registry must contain at least one source");
        }

        Map<SourceCode, MarketSource> sourcesMap = new LinkedHashMap<>();
        Map<SourceCode, SourcePassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketSource source : sources) {
            Objects.requireNonNull(source, "source must not be null");

            SourceCode code = Objects.requireNonNull(source.sourceCode(),
                    "source.sourceCode must not be null");

            SourcePassport passport = Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            Objects.requireNonNull(source.policy(),
                    "source.policy must not be null");

            MarketSource prev = sourcesMap.put(code, source);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Duplicate market source code detected (sourceCode=" + code.value() + ")"
                );
            }

            SourceDisplayName displayName = Objects.requireNonNull(passport.displayName(),
                    "source.passport.displayName must not be null");

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate market source display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.sourcesByCode = Collections.unmodifiableMap(sourcesMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<SourceCode, MarketSource> sourcesByCode() {
        return sourcesByCode;
    }

    @Override
    public Map<SourceCode, SourcePassport> passportsByCode() {
        return passportsByCode;
    }
}
