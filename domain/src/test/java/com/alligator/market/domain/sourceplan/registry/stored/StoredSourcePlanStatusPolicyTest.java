package com.alligator.market.domain.sourceplan.registry.stored;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoredSourcePlanStatusPolicyTest {

    private final StoredSourcePlanStatusPolicy policy = new StoredSourcePlanStatusPolicy();

    @Test
    void resolvesEntryLifecycleStatusFromSourceRegistration() {
        assertEquals(
                StoredSourcePlanEntryLifecycleStatus.AVAILABLE,
                policy.resolveEntryLifecycleStatus(true)
        );
        assertEquals(
                StoredSourcePlanEntryLifecycleStatus.SOURCE_RETIRED,
                policy.resolveEntryLifecycleStatus(false)
        );
    }

    @Test
    void resolvesPlanExecutionStatusFromCapturerRegistrationAndAvailableSources() {
        assertEquals(
                StoredSourcePlanExecutionStatus.CAPTURER_RETIRED,
                policy.resolvePlanExecutionStatus(false, true)
        );
        assertEquals(
                StoredSourcePlanExecutionStatus.NO_AVAILABLE_SOURCES,
                policy.resolvePlanExecutionStatus(true, false)
        );
        assertEquals(
                StoredSourcePlanExecutionStatus.AVAILABLE,
                policy.resolvePlanExecutionStatus(true, true)
        );
    }
}
