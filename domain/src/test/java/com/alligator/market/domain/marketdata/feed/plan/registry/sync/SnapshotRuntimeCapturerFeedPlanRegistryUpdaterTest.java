package com.alligator.market.domain.marketdata.feed.plan.registry.sync;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.plan.CapturerFeedPlan;
import com.alligator.market.domain.marketdata.feed.plan.registry.runtime.RuntimeCapturerFeedPlanRegistry;
import com.alligator.market.domain.marketdata.feed.plan.registry.stored.StoredCapturerFeedPlanRegistry;
import com.alligator.market.domain.marketdata.feed.plan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotRuntimeCapturerFeedPlanRegistryUpdaterTest {

    @Test
    void publishesAvailablePlansFromStoredRegistryAsRuntimeSnapshot() {
        CapturerFeedPlan plan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        CapturingRuntimeCapturerFeedPlanRegistryPublisher publisher =
                new CapturingRuntimeCapturerFeedPlanRegistryPublisher();
        RuntimeCapturerFeedPlanRegistryUpdater updater = new SnapshotRuntimeCapturerFeedPlanRegistryUpdater(
                new StubStoredCapturerFeedPlanRegistry(List.of(plan)),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertEquals(
                Optional.of(plan),
                publisher.publishedRegistry.findAvailableByCapturerCodeAndInstrumentCode(
                        plan.capturerCode(),
                        plan.instrumentCode()
                )
        );
        assertEquals(List.of(plan), publisher.publishedRegistry.findAvailableByCapturerCode(plan.capturerCode()));
        assertEquals(List.of(plan), publisher.publishedRegistry.findAllAvailable());
    }

    @Test
    void publishesEmptySnapshotWhenStoredRegistryHasNoAvailablePlans() {
        CapturingRuntimeCapturerFeedPlanRegistryPublisher publisher =
                new CapturingRuntimeCapturerFeedPlanRegistryPublisher();
        RuntimeCapturerFeedPlanRegistryUpdater updater = new SnapshotRuntimeCapturerFeedPlanRegistryUpdater(
                new StubStoredCapturerFeedPlanRegistry(List.of()),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertTrue(publisher.publishedRegistry.findAllAvailable().isEmpty());
    }

    @Test
    void replacesRuntimeSnapshotWhenStoredPlansChange() {
        CapturerFeedPlan firstPlan = plan("TEST_CAPTURER", "EUR_USD", "PRIMARY_SOURCE");
        CapturerFeedPlan changedPlan = plan("TEST_CAPTURER", "EUR_USD", "BACKUP_SOURCE");
        StubStoredCapturerFeedPlanRegistry storedRegistry =
                new StubStoredCapturerFeedPlanRegistry(List.of(firstPlan));
        CapturingRuntimeCapturerFeedPlanRegistryPublisher publisher =
                new CapturingRuntimeCapturerFeedPlanRegistryPublisher();
        RuntimeCapturerFeedPlanRegistryUpdater updater = new SnapshotRuntimeCapturerFeedPlanRegistryUpdater(
                storedRegistry,
                publisher
        );

        updater.updateRuntimeRegistry();

        assertEquals(
                Optional.of(firstPlan),
                publisher.publishedRegistry.findAvailableByCapturerCodeAndInstrumentCode(
                        firstPlan.capturerCode(),
                        firstPlan.instrumentCode()
                )
        );

        storedRegistry.replaceAvailablePlans(List.of(changedPlan));
        updater.updateRuntimeRegistry();

        assertEquals(
                Optional.of(changedPlan),
                publisher.publishedRegistry.findAvailableByCapturerCodeAndInstrumentCode(
                        changedPlan.capturerCode(),
                        changedPlan.instrumentCode()
                )
        );
        assertEquals(
                List.of(changedPlan),
                publisher.publishedRegistry.findAvailableByCapturerCode(changedPlan.capturerCode())
        );
    }

    private static CapturerFeedPlan plan(
            String capturerCode,
            String instrumentCode,
            String sourceCode
    ) {
        return new CapturerFeedPlan(
                CapturerCode.of(capturerCode),
                InstrumentCode.of(instrumentCode),
                List.of(new PrioritizedSourceCode(SourceCode.of(sourceCode), 0))
        );
    }

    private static final class StubStoredCapturerFeedPlanRegistry implements StoredCapturerFeedPlanRegistry {
        private List<CapturerFeedPlan> availablePlans;

        private StubStoredCapturerFeedPlanRegistry(List<CapturerFeedPlan> availablePlans) {
            replaceAvailablePlans(availablePlans);
        }

        private void replaceAvailablePlans(List<CapturerFeedPlan> availablePlans) {
            this.availablePlans = List.copyOf(
                    Objects.requireNonNull(availablePlans, "availablePlans must not be null")
            );
        }

        @Override
        public Optional<CapturerFeedPlan> findAvailableByCapturerCodeAndInstrumentCode(
                CapturerCode capturerCode,
                InstrumentCode instrumentCode
        ) {
            return availablePlans.stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .filter(plan -> plan.instrumentCode().equals(instrumentCode))
                    .findFirst();
        }

        @Override
        public List<CapturerFeedPlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
            return availablePlans.stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public List<CapturerFeedPlan> findAllAvailable() {
            return availablePlans;
        }
    }

    private static final class CapturingRuntimeCapturerFeedPlanRegistryPublisher
            implements RuntimeCapturerFeedPlanRegistryPublisher {
        private RuntimeCapturerFeedPlanRegistry publishedRegistry;

        @Override
        public void replaceWith(RuntimeCapturerFeedPlanRegistry registry) {
            publishedRegistry = Objects.requireNonNull(registry, "registry must not be null");
        }
    }
}
