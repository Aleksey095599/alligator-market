package com.alligator.market.domain.source.passport.store.sync;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.store.SourcePassportRecord;
import com.alligator.market.domain.source.passport.store.SourcePassportStore;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotSourcePassportStoreSynchronizer
        implements SourcePassportStoreSynchronizer {
    private final RuntimeSourceRegistry sourceRegistry;
    private final SourcePassportStore passportStore;

    public SnapshotSourcePassportStoreSynchronizer(
            RuntimeSourceRegistry sourceRegistry,
            SourcePassportStore passportStore
    ) {
        this.sourceRegistry = Objects.requireNonNull(
                sourceRegistry,
                "sourceRegistry must not be null"
        );
        this.passportStore = Objects.requireNonNull(
                passportStore,
                "passportStore must not be null"
        );
    }

    @Override
    public void synchronizeStoreFromSourceRegistry() {
        Map<SourceCode, SourcePassport> passportSnapshot = loadPassportSnapshotFromSourceRegistry();

        retirePassportsMissingFromSnapshot(passportSnapshot.keySet());
        saveRegisteredSnapshotRecords(passportSnapshot);
    }

    private Map<SourceCode, SourcePassport> loadPassportSnapshotFromSourceRegistry() {
        Map<SourceCode, MarketDataSource> sourceSnapshot = new LinkedHashMap<>(
                Objects.requireNonNull(
                        sourceRegistry.sourcesByCode(),
                        "sourcesByCode must not be null"
                )
        );

        if (sourceSnapshot.isEmpty()) {
            throw new IllegalStateException("Source registry returned no sources");
        }

        return passportsBySourceCode(sourceSnapshot);
    }

    private Map<SourceCode, SourcePassport> passportsBySourceCode(
            Map<SourceCode, MarketDataSource> sourceSnapshot
    ) {
        Map<SourceCode, SourcePassport> passportsByCode = new LinkedHashMap<>(sourceSnapshot.size());
        Set<String> displayNamesLower = new HashSet<>();

        for (Map.Entry<SourceCode, MarketDataSource> entry : sourceSnapshot.entrySet()) {
            SourceCode sourceCode = Objects.requireNonNull(
                    entry.getKey(),
                    "sourcesByCode must not contain null keys"
            );
            MarketDataSource source = Objects.requireNonNull(
                    entry.getValue(),
                    "sourcesByCode must not contain null values"
            );
            SourcePassport passport = Objects.requireNonNull(
                    source.passport(),
                    "source.passport must not be null"
            );

            requireUniqueDisplayName(passport, displayNamesLower);
            passportsByCode.put(sourceCode, passport);
        }

        return Collections.unmodifiableMap(passportsByCode);
    }

    private static void requireUniqueDisplayName(
            SourcePassport passport,
            Set<String> displayNamesLower
    ) {
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
    }

    private void retirePassportsMissingFromSnapshot(Set<SourceCode> registeredSourceCodes) {
        passportStore.retireAllExcept(registeredSourceCodes);
    }

    private void saveRegisteredSnapshotRecords(Map<SourceCode, SourcePassport> passportSnapshot) {
        passportStore.save(toRegisteredRecords(passportSnapshot));
    }

    private List<SourcePassportRecord> toRegisteredRecords(
            Map<SourceCode, SourcePassport> passportSnapshot
    ) {
        List<SourcePassportRecord> records = new ArrayList<>(passportSnapshot.size());
        for (Map.Entry<SourceCode, SourcePassport> entry : passportSnapshot.entrySet()) {
            records.add(SourcePassportRecord.registered(entry.getKey(), entry.getValue()));
        }

        return List.copyOf(records);
    }
}
