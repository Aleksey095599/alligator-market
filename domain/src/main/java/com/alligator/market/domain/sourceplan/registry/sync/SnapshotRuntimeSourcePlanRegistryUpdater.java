package com.alligator.market.domain.sourceplan.registry.sync;

import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.runtime.SnapshotRuntimeSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;

import java.util.List;
import java.util.Objects;

public final class SnapshotRuntimeSourcePlanRegistryUpdater implements RuntimeSourcePlanRegistryUpdater {
    private final StoredSourcePlanRegistry storedRegistry;
    private final RuntimeSourcePlanRegistryPublisher runtimeRegistryPublisher;

    public SnapshotRuntimeSourcePlanRegistryUpdater(
            StoredSourcePlanRegistry storedRegistry,
            RuntimeSourcePlanRegistryPublisher runtimeRegistryPublisher
    ) {
        this.storedRegistry = Objects.requireNonNull(
                storedRegistry,
                "storedRegistry must not be null"
        );
        this.runtimeRegistryPublisher = Objects.requireNonNull(
                runtimeRegistryPublisher,
                "runtimeRegistryPublisher must not be null"
        );
    }

    @Override
    public void updateRuntimeRegistry() {
        List<SourcePlan> executablePlans = loadExecutablePlans();
        RuntimeSourcePlanRegistry snapshot = createSnapshot(executablePlans);
        publishSnapshot(snapshot);
    }

    private List<SourcePlan> loadExecutablePlans() {
        return storedRegistry.findAllExecutable();
    }

    private RuntimeSourcePlanRegistry createSnapshot(List<SourcePlan> executablePlans) {
        return new SnapshotRuntimeSourcePlanRegistry(executablePlans);
    }

    private void publishSnapshot(RuntimeSourcePlanRegistry snapshot) {
        runtimeRegistryPublisher.replaceWith(snapshot);
    }
}
