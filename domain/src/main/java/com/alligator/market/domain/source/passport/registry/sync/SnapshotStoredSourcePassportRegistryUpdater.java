package com.alligator.market.domain.source.passport.registry.sync;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotStoredSourcePassportRegistryUpdater
        implements StoredSourcePassportRegistryUpdater {
    private final RuntimeSourcePassportRegistry runtimeRegistry;
    private final StoredSourcePassportRegistry storedRegistry;

    public SnapshotStoredSourcePassportRegistryUpdater(
            RuntimeSourcePassportRegistry runtimeRegistry,
            StoredSourcePassportRegistry storedRegistry
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
        Map<SourceCode, SourcePassport> runtimePassports = loadRuntimePassports();
        Set<SourceCode> runtimePassportCodes = extractRuntimePassportCodes(runtimePassports);

        retireMissingPassports(runtimePassportCodes);
        saveRegisteredPassports(runtimePassports);
    }

    private Map<SourceCode, SourcePassport> loadRuntimePassports() {
        Map<SourceCode, SourcePassport> runtimePassports = Map.copyOf(
                Objects.requireNonNull(runtimeRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (runtimePassports.isEmpty()) {
            throw new IllegalStateException("Runtime source passport registry returned no source passports");
        }

        return runtimePassports;
    }

    private Set<SourceCode> extractRuntimePassportCodes(
            Map<SourceCode, SourcePassport> runtimePassports
    ) {
        return Set.copyOf(runtimePassports.keySet());
    }

    private void retireMissingPassports(Set<SourceCode> runtimePassportCodes) {
        storedRegistry.retireAllExcept(runtimePassportCodes);
    }

    private void saveRegisteredPassports(Map<SourceCode, SourcePassport> runtimePassports) {
        storedRegistry.saveRegistered(runtimePassports);
    }
}
