package com.alligator.market.domain.sourceplan.registry.stored;

public final class StoredSourcePlanStatusPolicy {

    public StoredSourcePlanEntryLifecycleStatus resolveEntryLifecycleStatus(
            boolean sourceRegistered
    ) {
        if (sourceRegistered) {
            return StoredSourcePlanEntryLifecycleStatus.AVAILABLE;
        }

        return StoredSourcePlanEntryLifecycleStatus.SOURCE_RETIRED;
    }

    public StoredSourcePlanExecutionStatus resolvePlanExecutionStatus(
            boolean capturerRegistered,
            boolean hasAvailableSources
    ) {
        if (!capturerRegistered) {
            return StoredSourcePlanExecutionStatus.CAPTURER_RETIRED;
        }

        if (!hasAvailableSources) {
            return StoredSourcePlanExecutionStatus.NO_AVAILABLE_SOURCES;
        }

        return StoredSourcePlanExecutionStatus.AVAILABLE;
    }
}
