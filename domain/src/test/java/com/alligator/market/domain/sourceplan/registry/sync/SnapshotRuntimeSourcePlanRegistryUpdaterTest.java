package com.alligator.market.domain.sourceplan.registry.sync;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotRuntimeSourcePlanRegistryUpdaterTest {

    @Test
    void publishesExecutablePlansFromStoredRegistryAsRuntimeSnapshot() {
        SourcePlan plan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        CapturingRuntimeSourcePlanRegistryPublisher publisher =
                new CapturingRuntimeSourcePlanRegistryPublisher();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                new StubStoredSourcePlanRegistry(List.of(plan)),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertEquals(Optional.of(plan), publisher.publishedRegistry.findExecutableByKey(plan.key()));
        assertEquals(List.of(plan), publisher.publishedRegistry.findExecutableByCapturerCode(plan.capturerCode()));
        assertEquals(plan, publisher.publishedRegistry.executablePlansByKey().get(plan.key()));
    }

    @Test
    void publishesEmptySnapshotWhenStoredRegistryHasNoExecutablePlans() {
        CapturingRuntimeSourcePlanRegistryPublisher publisher =
                new CapturingRuntimeSourcePlanRegistryPublisher();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                new StubStoredSourcePlanRegistry(List.of()),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertTrue(publisher.publishedRegistry.executablePlansByKey().isEmpty());
    }

    @Test
    void replacesRuntimeSnapshotWhenStoredPlansChange() {
        SourcePlan firstPlan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        SourcePlan changedPlan = plan("TEST_CAPTURER", "EUR_USD", "BACKUP_SOURCE");
        StubStoredSourcePlanRegistry storedRegistry =
                new StubStoredSourcePlanRegistry(List.of(firstPlan));
        CapturingRuntimeSourcePlanRegistryPublisher publisher =
                new CapturingRuntimeSourcePlanRegistryPublisher();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                storedRegistry,
                publisher
        );

        updater.updateRuntimeRegistry();

        assertEquals(Optional.of(firstPlan), publisher.publishedRegistry.findExecutableByKey(firstPlan.key()));

        storedRegistry.replaceExecutablePlans(List.of(changedPlan));
        updater.updateRuntimeRegistry();

        assertEquals(Optional.of(changedPlan), publisher.publishedRegistry.findExecutableByKey(changedPlan.key()));
        assertEquals(List.of(changedPlan), publisher.publishedRegistry.findExecutableByCapturerCode(
                changedPlan.capturerCode()
        ));
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

    private static final class StubStoredSourcePlanRegistry implements StoredSourcePlanRegistry {
        private List<SourcePlan> executablePlans;

        private StubStoredSourcePlanRegistry(List<SourcePlan> executablePlans) {
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

    private static final class CapturingRuntimeSourcePlanRegistryPublisher
            implements RuntimeSourcePlanRegistryPublisher {
        private RuntimeSourcePlanRegistry publishedRegistry;

        @Override
        public void replaceWith(RuntimeSourcePlanRegistry registry) {
            publishedRegistry = Objects.requireNonNull(registry, "registry must not be null");
        }
    }
}
