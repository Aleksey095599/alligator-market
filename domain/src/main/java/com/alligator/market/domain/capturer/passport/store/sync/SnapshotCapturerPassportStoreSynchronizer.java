package com.alligator.market.domain.capturer.passport.store.sync;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportStore;
import com.alligator.market.domain.capturer.registry.CapturerRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotCapturerPassportStoreSynchronizer
        implements CapturerPassportStoreSynchronizer {
    private final CapturerRegistry capturerRegistry;
    private final CapturerPassportStore passportStore;

    public SnapshotCapturerPassportStoreSynchronizer(
            CapturerRegistry capturerRegistry,
            CapturerPassportStore passportStore
    ) {
        this.capturerRegistry = Objects.requireNonNull(
                capturerRegistry,
                "capturerRegistry must not be null"
        );
        this.passportStore = Objects.requireNonNull(
                passportStore,
                "passportStore must not be null"
        );
    }

    @Override
    public void synchronizeStoreFromCapturerRegistry() {
        Map<CapturerCode, CapturerPassport> passportSnapshot = loadPassportSnapshotFromCapturerRegistry();

        retirePassportsMissingFromSnapshot(passportSnapshot.keySet());
        saveRegisteredSnapshotRecords(passportSnapshot);
    }

    private Map<CapturerCode, CapturerPassport> loadPassportSnapshotFromCapturerRegistry() {
        Map<CapturerCode, MarketDataCapturer> capturerSnapshot = new LinkedHashMap<>(
                Objects.requireNonNull(
                        capturerRegistry.capturersByCode(),
                        "capturersByCode must not be null"
                )
        );

        if (capturerSnapshot.isEmpty()) {
            throw new IllegalStateException("Capturer registry returned no capturers");
        }

        return passportsByCapturerCode(capturerSnapshot);
    }

    private Map<CapturerCode, CapturerPassport> passportsByCapturerCode(
            Map<CapturerCode, MarketDataCapturer> capturerSnapshot
    ) {
        Map<CapturerCode, CapturerPassport> passportsByCode = new LinkedHashMap<>(capturerSnapshot.size());
        Set<String> displayNamesLower = new HashSet<>();

        for (Map.Entry<CapturerCode, MarketDataCapturer> entry : capturerSnapshot.entrySet()) {
            CapturerCode capturerCode = Objects.requireNonNull(
                    entry.getKey(),
                    "capturersByCode must not contain null keys"
            );
            MarketDataCapturer capturer = Objects.requireNonNull(
                    entry.getValue(),
                    "capturersByCode must not contain null values"
            );
            CapturerPassport passport = Objects.requireNonNull(
                    capturer.passport(),
                    "capturer.passport must not be null"
            );

            requireUniqueDisplayName(passport, displayNamesLower);
            passportsByCode.put(capturerCode, passport);
        }

        return Collections.unmodifiableMap(passportsByCode);
    }

    private static void requireUniqueDisplayName(
            CapturerPassport passport,
            Set<String> displayNamesLower
    ) {
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
    }

    private void retirePassportsMissingFromSnapshot(Set<CapturerCode> registeredCapturerCodes) {
        passportStore.retireAllExcept(registeredCapturerCodes);
    }

    private void saveRegisteredSnapshotRecords(Map<CapturerCode, CapturerPassport> passportSnapshot) {
        passportStore.save(toRegisteredRecords(passportSnapshot));
    }

    private List<CapturerPassportRecord> toRegisteredRecords(
            Map<CapturerCode, CapturerPassport> passportSnapshot
    ) {
        List<CapturerPassportRecord> records = new ArrayList<>(passportSnapshot.size());
        for (Map.Entry<CapturerCode, CapturerPassport> entry : passportSnapshot.entrySet()) {
            records.add(CapturerPassportRecord.registered(entry.getKey(), entry.getValue()));
        }

        return List.copyOf(records);
    }
}
