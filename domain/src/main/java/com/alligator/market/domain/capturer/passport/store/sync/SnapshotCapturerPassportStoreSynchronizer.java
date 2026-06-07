package com.alligator.market.domain.capturer.passport.store.sync;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.registry.CapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportStore;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotCapturerPassportStoreSynchronizer
        implements CapturerPassportStoreSynchronizer {
    private final CapturerPassportRegistry passportRegistry;
    private final CapturerPassportStore passportStore;

    public SnapshotCapturerPassportStoreSynchronizer(
            CapturerPassportRegistry passportRegistry,
            CapturerPassportStore passportStore
    ) {
        this.passportRegistry = Objects.requireNonNull(
                passportRegistry,
                "passportRegistry must not be null"
        );
        this.passportStore = Objects.requireNonNull(
                passportStore,
                "passportStore must not be null"
        );
    }

    @Override
    public void synchronize() {
        Map<CapturerCode, CapturerPassport> registrySnapshot = loadRegistrySnapshot();

        retirePassportsMissingFromSnapshot(registrySnapshot.keySet());
        saveRegisteredSnapshotRecords(registrySnapshot);
    }

    private Map<CapturerCode, CapturerPassport> loadRegistrySnapshot() {
        Map<CapturerCode, CapturerPassport> registrySnapshot = Map.copyOf(
                Objects.requireNonNull(passportRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (registrySnapshot.isEmpty()) {
            throw new IllegalStateException("Capturer passport registry returned no capturer passports");
        }

        return registrySnapshot;
    }

    private void retirePassportsMissingFromSnapshot(Set<CapturerCode> registeredCapturerCodes) {
        passportStore.retireAllExcept(registeredCapturerCodes);
    }

    private void saveRegisteredSnapshotRecords(Map<CapturerCode, CapturerPassport> registrySnapshot) {
        passportStore.save(toRegisteredRecords(registrySnapshot));
    }

    private List<CapturerPassportRecord> toRegisteredRecords(
            Map<CapturerCode, CapturerPassport> registrySnapshot
    ) {
        List<CapturerPassportRecord> records = new ArrayList<>(registrySnapshot.size());
        for (Map.Entry<CapturerCode, CapturerPassport> entry : registrySnapshot.entrySet()) {
            records.add(CapturerPassportRecord.registered(entry.getKey(), entry.getValue()));
        }

        return List.copyOf(records);
    }
}
