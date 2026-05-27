package com.alligator.market.backend.sourceplan.plan.registry.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.runtime.SnapshotRuntimeSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryPublisher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicRuntimeSourcePlanRegistry
        implements RuntimeSourcePlanRegistry, RuntimeSourcePlanRegistryPublisher {
    private final AtomicReference<RuntimeSourcePlanRegistry> currentRegistry =
            new AtomicReference<>(new SnapshotRuntimeSourcePlanRegistry(List.of()));

    @Override
    public Optional<SourcePlan> findAvailableByKey(SourcePlanKey key) {
        return currentRegistry.get().findAvailableByKey(key);
    }

    @Override
    public List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
        return currentRegistry.get().findAvailableByCapturerCode(capturerCode);
    }

    @Override
    public Map<SourcePlanKey, SourcePlan> availablePlansByKey() {
        return currentRegistry.get().availablePlansByKey();
    }

    @Override
    public void replaceWith(RuntimeSourcePlanRegistry registry) {
        currentRegistry.set(Objects.requireNonNull(registry, "registry must not be null"));
    }
}
