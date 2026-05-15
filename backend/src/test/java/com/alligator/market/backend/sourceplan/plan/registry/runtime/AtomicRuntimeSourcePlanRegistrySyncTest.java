package com.alligator.market.backend.sourceplan.plan.registry.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import com.alligator.market.domain.sourceplan.registry.sync.SnapshotRuntimeSourcePlanRegistryUpdater;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtomicRuntimeSourcePlanRegistrySyncTest {

    @Test
    void runtimeRegistryReturnsChangedPlanAfterUpdaterRunsAgain() {
        SourcePlan firstPlan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        SourcePlan changedPlan = plan("TEST_CAPTURER", "EUR_USD", "BACKUP_SOURCE");
        MutableStoredSourcePlanRegistry storedRegistry =
                new MutableStoredSourcePlanRegistry(List.of(firstPlan));
        AtomicRuntimeSourcePlanRegistry runtimeRegistry = new AtomicRuntimeSourcePlanRegistry();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                storedRegistry,
                runtimeRegistry
        );

        updater.updateRuntimeRegistry();

        assertEquals(Optional.of(firstPlan), runtimeRegistry.findExecutableByKey(firstPlan.key()));

        storedRegistry.replaceExecutablePlans(List.of(changedPlan));
        updater.updateRuntimeRegistry();

        assertEquals(Optional.of(changedPlan), runtimeRegistry.findExecutableByKey(changedPlan.key()));
        assertEquals(List.of(changedPlan), runtimeRegistry.findExecutableByCapturerCode(
                changedPlan.capturerCode()
        ));
    }

    @Test
    void runtimeRegistryIsClearedWhenStoredRegistryHasNoExecutablePlans() {
        SourcePlan plan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        MutableStoredSourcePlanRegistry storedRegistry =
                new MutableStoredSourcePlanRegistry(List.of(plan));
        AtomicRuntimeSourcePlanRegistry runtimeRegistry = new AtomicRuntimeSourcePlanRegistry();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                storedRegistry,
                runtimeRegistry
        );

        updater.updateRuntimeRegistry();
        storedRegistry.replaceExecutablePlans(List.of());
        updater.updateRuntimeRegistry();

        assertTrue(runtimeRegistry.executablePlansByKey().isEmpty());
        assertEquals(Optional.empty(), runtimeRegistry.findExecutableByKey(plan.key()));
    }

    private static SourcePlan plan(
            String capturerCode,
            String instrumentCode,
            String sourceCode
    ) {
        return new SourcePlan(
                CapturerCode.of(capturerCode),
                InstrumentCode.of(instrumentCode),
                List.of(new SourcePlanEntry(SourceCode.of(sourceCode), 0))
        );
    }

    private static final class MutableStoredSourcePlanRegistry implements StoredSourcePlanRegistry {
        private List<SourcePlan> executablePlans;

        private MutableStoredSourcePlanRegistry(List<SourcePlan> executablePlans) {
            replaceExecutablePlans(executablePlans);
        }

        private void replaceExecutablePlans(List<SourcePlan> executablePlans) {
            this.executablePlans = List.copyOf(
                    Objects.requireNonNull(executablePlans, "executablePlans must not be null")
            );
        }

        @Override
        public Optional<SourcePlan> findExecutableByKey(SourcePlanKey key) {
            return executablePlans.stream()
                    .filter(plan -> plan.key().equals(key))
                    .findFirst();
        }

        @Override
        public List<SourcePlan> findExecutableByCapturerCode(CapturerCode capturerCode) {
            return executablePlans.stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public List<SourcePlan> findAllExecutable() {
            return executablePlans;
        }
    }
}
