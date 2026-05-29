package com.alligator.market.domain.marketdata.feed.plan.registry.stored;

public final class StoredCapturerFeedPlanStatusPolicy {

    public StoredCapturerFeedPlanSourceLifecycleStatus resolveSourceLifecycleStatus(
            boolean sourceRegistered
    ) {
        if (sourceRegistered) {
            return StoredCapturerFeedPlanSourceLifecycleStatus.AVAILABLE;
        }

        return StoredCapturerFeedPlanSourceLifecycleStatus.SOURCE_RETIRED;
    }

    public StoredCapturerFeedPlanStatus resolvePlanStatus(
            boolean capturerRegistered,
            boolean hasAvailableSources
    ) {
        if (!capturerRegistered) {
            return StoredCapturerFeedPlanStatus.CAPTURER_RETIRED;
        }

        if (!hasAvailableSources) {
            return StoredCapturerFeedPlanStatus.NO_AVAILABLE_SOURCES;
        }

        return StoredCapturerFeedPlanStatus.AVAILABLE;
    }
}
