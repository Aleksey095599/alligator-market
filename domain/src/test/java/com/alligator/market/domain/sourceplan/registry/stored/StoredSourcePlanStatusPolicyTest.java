package com.alligator.market.domain.sourceplan.registry.stored;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoredSourcePlanStatusPolicyTest {

    private final StoredSourcePlanStatusPolicy policy = new StoredSourcePlanStatusPolicy();

    @Test
    void resolvesEntryLifecycleStatusFromSourceRegistration() {
        assertEquals(
                StoredSourcePlan.EntryLifecycleStatus.AVAILABLE,
                policy.resolveEntryLifecycleStatus(true)
        );
        assertEquals(
                StoredSourcePlan.EntryLifecycleStatus.SOURCE_RETIRED,
                policy.resolveEntryLifecycleStatus(false)
        );
    }

    @Test
    void resolvesPlanExecutionStatusFromCapturerRegistrationAndAvailableSources() {
        assertEquals(
                StoredSourcePlan.ExecutionStatus.CAPTURER_RETIRED,
                policy.resolvePlanExecutionStatus(false, true)
        );
        assertEquals(
                StoredSourcePlan.ExecutionStatus.NO_AVAILABLE_SOURCES,
                policy.resolvePlanExecutionStatus(true, false)
        );
        assertEquals(
                StoredSourcePlan.ExecutionStatus.AVAILABLE,
                policy.resolvePlanExecutionStatus(true, true)
        );
    }
}
