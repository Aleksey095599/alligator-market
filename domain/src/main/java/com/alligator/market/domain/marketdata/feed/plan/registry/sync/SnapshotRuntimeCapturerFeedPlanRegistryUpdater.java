package com.alligator.market.domain.marketdata.feed.plan.registry.sync;

import com.alligator.market.domain.marketdata.feed.plan.CapturerFeedPlan;
import com.alligator.market.domain.marketdata.feed.plan.registry.runtime.RuntimeCapturerFeedPlanRegistry;
import com.alligator.market.domain.marketdata.feed.plan.registry.runtime.SnapshotRuntimeCapturerFeedPlanRegistry;
import com.alligator.market.domain.marketdata.feed.plan.registry.stored.StoredCapturerFeedPlanRegistry;

import java.util.List;
import java.util.Objects;

public final class SnapshotRuntimeCapturerFeedPlanRegistryUpdater
        implements RuntimeCapturerFeedPlanRegistryUpdater {
    private final StoredCapturerFeedPlanRegistry storedRegistry;
    private final RuntimeCapturerFeedPlanRegistryPublisher runtimeRegistryPublisher;

    public SnapshotRuntimeCapturerFeedPlanRegistryUpdater(
            StoredCapturerFeedPlanRegistry storedRegistry,
            RuntimeCapturerFeedPlanRegistryPublisher runtimeRegistryPublisher
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
        List<CapturerFeedPlan> availablePlans = loadAvailablePlans();
        RuntimeCapturerFeedPlanRegistry snapshot = createSnapshot(availablePlans);
        publishSnapshot(snapshot);
    }

    private List<CapturerFeedPlan> loadAvailablePlans() {
        return storedRegistry.findAllAvailable();
    }

    private RuntimeCapturerFeedPlanRegistry createSnapshot(List<CapturerFeedPlan> availablePlans) {
        return new SnapshotRuntimeCapturerFeedPlanRegistry(availablePlans);
    }

    private void publishSnapshot(RuntimeCapturerFeedPlanRegistry snapshot) {
        runtimeRegistryPublisher.replaceWith(snapshot);
    }
}
