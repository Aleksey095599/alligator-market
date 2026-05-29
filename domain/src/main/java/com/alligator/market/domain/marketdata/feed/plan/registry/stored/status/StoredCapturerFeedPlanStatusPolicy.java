package com.alligator.market.domain.marketdata.feed.plan.registry.stored.status;

import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistryStatus;

import java.util.Collection;
import java.util.Objects;

public final class StoredCapturerFeedPlanStatusPolicy {

    public StoredCapturerFeedPlanStatus resolvePlanStatus(
            boolean capturerRegistered,
            Collection<StoredSourcePassportRegistryStatus> plannedSourcePassportRegistryStatuses
    ) {
        Objects.requireNonNull(
                plannedSourcePassportRegistryStatuses,
                "plannedSourcePassportRegistryStatuses must not be null"
        );

        if (!capturerRegistered) {
            return StoredCapturerFeedPlanStatus.CAPTURER_RETIRED;
        }

        if (!hasRegisteredSourcePassport(plannedSourcePassportRegistryStatuses)) {
            return StoredCapturerFeedPlanStatus.NO_AVAILABLE_SOURCES;
        }

        return StoredCapturerFeedPlanStatus.AVAILABLE;
    }

    private static boolean hasRegisteredSourcePassport(
            Collection<StoredSourcePassportRegistryStatus> plannedSourcePassportRegistryStatuses
    ) {
        return plannedSourcePassportRegistryStatuses.stream()
                .map(status -> Objects.requireNonNull(status, "sourcePassportRegistryStatus must not be null"))
                .anyMatch(StoredSourcePassportRegistryStatus.REGISTERED::equals);
    }
}
