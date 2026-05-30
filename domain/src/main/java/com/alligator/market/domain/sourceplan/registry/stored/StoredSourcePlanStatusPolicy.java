package com.alligator.market.domain.sourceplan.registry.stored;

public final class StoredSourcePlanStatusPolicy {

    public StoredSourcePlan.EntryLifecycleStatus resolveEntryLifecycleStatus(
            boolean sourceRegistered
    ) {
        if (sourceRegistered) {
            return StoredSourcePlan.EntryLifecycleStatus.AVAILABLE;
        }

        return StoredSourcePlan.EntryLifecycleStatus.SOURCE_RETIRED;
    }

    public StoredSourcePlan.ExecutionStatus resolvePlanExecutionStatus(
            boolean capturerRegistered,
            boolean hasAvailableSources
    ) {
        if (!capturerRegistered) {
            return StoredSourcePlan.ExecutionStatus.CAPTURER_RETIRED;
        }

        if (!hasAvailableSources) {
            return StoredSourcePlan.ExecutionStatus.NO_AVAILABLE_SOURCES;
        }

        return StoredSourcePlan.ExecutionStatus.AVAILABLE;
    }
}
