package com.alligator.market.domain.marketdata.feed.plan.registry.stored;

import com.alligator.market.domain.marketdata.feed.plan.registry.stored.status.StoredCapturerFeedPlanStatus;
import com.alligator.market.domain.marketdata.feed.plan.registry.stored.status.StoredCapturerFeedPlanStatusPolicy;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoredCapturerFeedPlanStatusPolicyTest {

    private final StoredCapturerFeedPlanStatusPolicy policy = new StoredCapturerFeedPlanStatusPolicy();

    @Test
    void resolvesPlanStatusFromCapturerRegistrationAndSourcePassportStatuses() {
        assertEquals(
                StoredCapturerFeedPlanStatus.CAPTURER_RETIRED,
                policy.resolvePlanStatus(false, List.of(StoredSourcePassport.RegistryStatus.REGISTERED))
        );
        assertEquals(
                StoredCapturerFeedPlanStatus.NO_AVAILABLE_SOURCES,
                policy.resolvePlanStatus(true, List.of(StoredSourcePassport.RegistryStatus.RETIRED))
        );
        assertEquals(
                StoredCapturerFeedPlanStatus.NO_AVAILABLE_SOURCES,
                policy.resolvePlanStatus(true, List.of())
        );
        assertEquals(
                StoredCapturerFeedPlanStatus.AVAILABLE,
                policy.resolvePlanStatus(true, List.of(
                        StoredSourcePassport.RegistryStatus.RETIRED,
                        StoredSourcePassport.RegistryStatus.REGISTERED
                ))
        );
    }
}
