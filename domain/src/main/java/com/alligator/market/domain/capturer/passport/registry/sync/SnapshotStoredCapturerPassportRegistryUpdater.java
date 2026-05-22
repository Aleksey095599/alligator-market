package com.alligator.market.domain.capturer.passport.registry.sync;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.registry.runtime.RuntimeCapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotStoredCapturerPassportRegistryUpdater
        implements StoredCapturerPassportRegistryUpdater {
    private final RuntimeCapturerPassportRegistry runtimeRegistry;
    private final StoredCapturerPassportRegistry storedRegistry;

    public SnapshotStoredCapturerPassportRegistryUpdater(
            RuntimeCapturerPassportRegistry runtimeRegistry,
            StoredCapturerPassportRegistry storedRegistry
    ) {
        this.runtimeRegistry = Objects.requireNonNull(
                runtimeRegistry,
                "runtimeRegistry must not be null"
        );
        this.storedRegistry = Objects.requireNonNull(
                storedRegistry,
                "storedRegistry must not be null"
        );
    }

    @Override
    public void updateStoredRegistry() {
        Map<CapturerCode, CapturerPassport> runtimePassports = loadRuntimePassports();
        Set<CapturerCode> runtimePassportCodes = extractRuntimePassportCodes(runtimePassports);

        retireMissingPassports(runtimePassportCodes);
        saveRegisteredPassports(runtimePassports);
    }

    private Map<CapturerCode, CapturerPassport> loadRuntimePassports() {
        Map<CapturerCode, CapturerPassport> runtimePassports = Map.copyOf(
                Objects.requireNonNull(runtimeRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (runtimePassports.isEmpty()) {
            throw new IllegalStateException("Runtime capturer passport registry returned no capturer passports");
        }

        return runtimePassports;
    }

    private Set<CapturerCode> extractRuntimePassportCodes(
            Map<CapturerCode, CapturerPassport> runtimePassports
    ) {
        return Set.copyOf(runtimePassports.keySet());
    }

    private void retireMissingPassports(Set<CapturerCode> runtimePassportCodes) {
        storedRegistry.retireAllExcept(runtimePassportCodes);
    }

    private void saveRegisteredPassports(Map<CapturerCode, CapturerPassport> runtimePassports) {
        storedRegistry.saveRegistered(runtimePassports);
    }
}
