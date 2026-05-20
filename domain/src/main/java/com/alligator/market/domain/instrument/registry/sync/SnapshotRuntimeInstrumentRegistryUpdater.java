package com.alligator.market.domain.instrument.registry.sync;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.registry.runtime.SnapshotRuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.repository.InstrumentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SnapshotRuntimeInstrumentRegistryUpdater implements RuntimeInstrumentRegistryUpdater {
    private final List<InstrumentRepository<? extends Instrument>> repositories;
    private final RuntimeInstrumentRegistryPublisher runtimeRegistryPublisher;

    public SnapshotRuntimeInstrumentRegistryUpdater(
            List<InstrumentRepository<? extends Instrument>> repositories,
            RuntimeInstrumentRegistryPublisher runtimeRegistryPublisher
    ) {
        Objects.requireNonNull(repositories, "repositories must not be null");

        this.repositories = List.copyOf(repositories);
        this.runtimeRegistryPublisher = Objects.requireNonNull(
                runtimeRegistryPublisher,
                "runtimeRegistryPublisher must not be null"
        );
    }

    @Override
    public void updateRuntimeRegistry() {
        List<Instrument> instruments = loadInstruments();
        RuntimeInstrumentRegistry snapshot = createSnapshot(instruments);
        publishSnapshot(snapshot);
    }

    private List<Instrument> loadInstruments() {
        List<Instrument> instruments = new ArrayList<>();

        for (InstrumentRepository<? extends Instrument> repository : repositories) {
            InstrumentRepository<? extends Instrument> repositoryToRead = Objects.requireNonNull(
                    repository,
                    "repository must not be null"
            );
            instruments.addAll(repositoryToRead.findAll());
        }

        return instruments;
    }

    private RuntimeInstrumentRegistry createSnapshot(List<Instrument> instruments) {
        return new SnapshotRuntimeInstrumentRegistry(instruments);
    }

    private void publishSnapshot(RuntimeInstrumentRegistry snapshot) {
        runtimeRegistryPublisher.replaceWith(snapshot);
    }
}
