package com.alligator.market.domain.marketdata.feed.plan.registry.stored;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoredCapturerFeedPlanStatusPolicyTest {

    private final StoredCapturerFeedPlanStatusPolicy policy = new StoredCapturerFeedPlanStatusPolicy();

    @Test
    void resolvesSourceLifecycleStatusFromSourceRegistration() {
        assertEquals(
                StoredCapturerFeedPlanSourceLifecycleStatus.AVAILABLE,
                policy.resolveSourceLifecycleStatus(true)
        );
        assertEquals(
                StoredCapturerFeedPlanSourceLifecycleStatus.SOURCE_RETIRED,
                policy.resolveSourceLifecycleStatus(false)
        );
    }

    @Test
    void resolvesPlanStatusFromCapturerRegistrationAndAvailableSources() {
        assertEquals(
                StoredCapturerFeedPlanStatus.CAPTURER_RETIRED,
                policy.resolvePlanStatus(false, true)
        );
        assertEquals(
                StoredCapturerFeedPlanStatus.NO_AVAILABLE_SOURCES,
                policy.resolvePlanStatus(true, false)
        );
        assertEquals(
                StoredCapturerFeedPlanStatus.AVAILABLE,
                policy.resolvePlanStatus(true, true)
        );
    }
}
