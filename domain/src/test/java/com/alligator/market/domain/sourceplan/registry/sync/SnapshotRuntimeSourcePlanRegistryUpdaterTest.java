package com.alligator.market.domain.sourceplan.registry.sync;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
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
    void publishesAvailablePlansFromStoredRegistryAsRuntimeSnapshot() {
        SourcePlan plan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        CapturingRuntimeSourcePlanRegistryPublisher publisher =
                new CapturingRuntimeSourcePlanRegistryPublisher();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                new StubStoredSourcePlanRegistry(List.of(plan)),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertEquals(Optional.of(plan), publisher.publishedRegistry.findAvailableByKey(plan.key()));
        assertEquals(List.of(plan), publisher.publishedRegistry.findAvailableByCapturerCode(plan.capturerCode()));
        assertEquals(plan, publisher.publishedRegistry.availablePlansByKey().get(plan.key()));
    }

    @Test
    void publishesEmptySnapshotWhenStoredRegistryHasNoAvailablePlans() {
        CapturingRuntimeSourcePlanRegistryPublisher publisher =
                new CapturingRuntimeSourcePlanRegistryPublisher();
        RuntimeSourcePlanRegistryUpdater updater = new SnapshotRuntimeSourcePlanRegistryUpdater(
                new StubStoredSourcePlanRegistry(List.of()),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertTrue(publisher.publishedRegistry.availablePlansByKey().isEmpty());
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

        assertEquals(Optional.of(firstPlan), publisher.publishedRegistry.findAvailableByKey(firstPlan.key()));

        storedRegistry.replaceAvailablePlans(List.of(changedPlan));
        updater.updateRuntimeRegistry();

        assertEquals(Optional.of(changedPlan), publisher.publishedRegistry.findAvailableByKey(changedPlan.key()));
        assertEquals(List.of(changedPlan), publisher.publishedRegistry.findAvailableByCapturerCode(
                changedPlan.capturerCode()
        ));
    }

    private static SourcePlan plan(
            String capturerCode,
            String instrumentCode,
            String sourceCode
    ) {
        return new SourcePlan(
                new SourcePlanKey(
                        CapturerCode.of(capturerCode),
                        InstrumentCode.of(instrumentCode)
                ),
                List.of(new PrioritizedSourceCode(SourceCode.of(sourceCode), 0))
        );
    }

    private static final class StubStoredSourcePlanRegistry implements StoredSourcePlanRegistry {
        private List<SourcePlan> availablePlans;

        private StubStoredSourcePlanRegistry(List<SourcePlan> availablePlans) {
            replaceAvailablePlans(availablePlans);
        }

        private void replaceAvailablePlans(List<SourcePlan> availablePlans) {
            this.availablePlans = List.copyOf(
                    Objects.requireNonNull(availablePlans, "availablePlans must not be null")
            );
        }

        @Override
        public Optional<SourcePlan> findAvailableByKey(SourcePlanKey key) {
            return availablePlans.stream()
                    .filter(plan -> plan.key().equals(key))
                    .findFirst();
        }

        @Override
        public List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
            return availablePlans.stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public List<SourcePlan> findAllAvailable() {
            return availablePlans;
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
