package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* Тесты для проверки логики сравнения профилей. */
class ProviderProfilesReconciliationTest {

    @Disabled
    @Test
    void compareShouldDetectAddReplaceAndMissing() {
        ProviderProfile dbA = profile("A", "Alpha");
        ProviderProfile dbB = profile("B", "Beta");
        ProviderProfile dbC = profile("C", "Gamma");

        ProviderProfile ctxA = profile("A", "Alpha");
        ProviderProfile ctxB = profile("B", "BetaNew");
        ProviderProfile ctxD = profile("D", "Delta");

        TestScanner scanner = new TestScanner(List.of(ctxA, ctxB, ctxD));
        TestStorage storage = new TestStorage(Map.of(dbA, 1L, dbB, 2L, dbC, 3L));

        ProviderProfilesReconciliation service = new ProviderProfilesReconciliation(scanner, storage);
        ContextDiff diff = service.compare();

        assertTrue(diff.add().contains(ctxB));
        assertTrue(diff.add().contains(ctxD));
        assertEquals(2, diff.add().size());

        assertEquals(1, diff.replaced().size());
        assertTrue(diff.replaced().contains(2L));

        assertEquals(1, diff.missing().size());
        assertTrue(diff.missing().contains(3L));
    }

    private static ProviderProfile profile(String code, String name) {
        return new ProviderProfile(
                code,
                name,
                Set.of(InstrumentType.CURRENCY),
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                1
        );
    }

    private record TestScanner(List<ProviderProfile> profiles) implements ProviderContextScanner {
        @Override
        public List<ProviderProfile> getProviderProfiles() {
            return profiles;
        }
    }

    private record TestStorage(Map<ProviderProfile, Long> profiles) implements ProviderProfileStorage {
        @Override
        public Map<ProviderProfile, Long> findAllActive() {
            return profiles;
        }

        @Override
        public Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus() {
            // не требуется для тестов
            return Map.of();
        }

        @Override
        public void saveAll(java.util.Collection<ProviderProfile> profiles) {
            // не требуется для тестов
        }

        @Override
        public void updateStatus(java.util.Collection<Long> ids, ProviderProfileStatus status) {
            // не требуется для тестов
        }
    }
}
