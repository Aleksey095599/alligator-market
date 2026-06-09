package com.alligator.market.domain.capturer.passport.store.sync;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportStore;
import com.alligator.market.domain.capturer.registry.CapturerRegistry;
import com.alligator.market.domain.capturer.registry.SnapshotCapturerRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SnapshotCapturerPassportStoreSynchronizerTest {

    @Test
    void synchronizesStoreFromCapturerRegistrySnapshot() {
        CapturerCode code = CapturerCode.of("TEST_CAPTURER");
        CapturerPassport passport = passport("Test Capturer");
        CapturerRegistry capturerRegistry = new SnapshotCapturerRegistry(List.of(
                new TestCapturer(code, passport)
        ));
        CapturingCapturerPassportStore passportStore = new CapturingCapturerPassportStore();
        CapturerPassportStoreSynchronizer synchronizer = new SnapshotCapturerPassportStoreSynchronizer(
                capturerRegistry,
                passportStore
        );

        synchronizer.synchronizeStoreFromCapturerRegistry();

        assertEquals(Set.of(code), passportStore.retiredAllExcept);
        assertEquals(
                List.of(CapturerPassportRecord.registered(code, passport)),
                passportStore.savedRecords
        );
    }

    @Test
    void rejectsDuplicatePassportDisplayNames() {
        CapturerRegistry capturerRegistry = new SnapshotCapturerRegistry(List.of(
                new TestCapturer(CapturerCode.of("FIRST_CAPTURER"), passport("Duplicate")),
                new TestCapturer(CapturerCode.of("SECOND_CAPTURER"), passport("duplicate"))
        ));
        CapturerPassportStoreSynchronizer synchronizer = new SnapshotCapturerPassportStoreSynchronizer(
                capturerRegistry,
                new CapturingCapturerPassportStore()
        );

        assertThrows(IllegalArgumentException.class, synchronizer::synchronizeStoreFromCapturerRegistry);
    }

    private static CapturerPassport passport(String displayName) {
        return new CapturerPassport(CapturerDisplayName.of(displayName), displayName + " description");
    }

    private record TestCapturer(
            CapturerCode capturerCode,
            CapturerPassport passport
    ) implements MarketDataCapturer {
    }

    private static final class CapturingCapturerPassportStore implements CapturerPassportStore {
        private Set<CapturerCode> retiredAllExcept = Set.of();
        private List<CapturerPassportRecord> savedRecords = List.of();

        @Override
        public void retireAllExcept(Set<CapturerCode> registeredCapturerCodes) {
            retiredAllExcept = new LinkedHashSet<>(registeredCapturerCodes);
        }

        @Override
        public void save(Collection<CapturerPassportRecord> passports) {
            savedRecords = List.copyOf(passports);
        }
    }
}
