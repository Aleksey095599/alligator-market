package com.alligator.market.domain.sourceplan.registry.stored;

public final class StoredSourcePlanStatusPolicy {

    public StoredSourcePlanEntryLifecycleStatus resolveEntryLifecycleStatus(
            boolean capturerActive,
            boolean sourceActive
    ) {
        if (capturerActive && sourceActive) {
            return StoredSourcePlanEntryLifecycleStatus.ACTIVE;
        }

        return StoredSourcePlanEntryLifecycleStatus.RETIRED;
    }

    public StoredSourcePlanExecutionStatus resolvePlanExecutionStatus(
            boolean capturerActive,
            boolean hasExecutableSources
    ) {
        if (!capturerActive) {
            return StoredSourcePlanExecutionStatus.CAPTURER_RETIRED;
        }

        if (!hasExecutableSources) {
            return StoredSourcePlanExecutionStatus.NO_EXECUTABLE_SOURCES;
        }

        return StoredSourcePlanExecutionStatus.EXECUTABLE;
    }
}
