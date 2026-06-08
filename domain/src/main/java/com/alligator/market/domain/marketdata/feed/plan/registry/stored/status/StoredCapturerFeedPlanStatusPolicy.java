package com.alligator.market.domain.marketdata.feed.plan.registry.stored.status;

import com.alligator.market.domain.source.passport.store.SourcePassportRecord;

import java.util.Collection;
import java.util.Objects;

public final class StoredCapturerFeedPlanStatusPolicy {

    public StoredCapturerFeedPlanStatus resolvePlanStatus(
            boolean capturerRegistered,
            Collection<SourcePassportRecord.RegistryStatus> plannedSourcePassportStatuses
    ) {
        Objects.requireNonNull(
                plannedSourcePassportStatuses,
                "plannedSourcePassportStatuses must not be null"
        );

        if (!capturerRegistered) {
            return StoredCapturerFeedPlanStatus.CAPTURER_RETIRED;
        }

        if (!hasRegisteredSourcePassport(plannedSourcePassportStatuses)) {
            return StoredCapturerFeedPlanStatus.NO_AVAILABLE_SOURCES;
        }

        return StoredCapturerFeedPlanStatus.AVAILABLE;
    }

    private static boolean hasRegisteredSourcePassport(
            Collection<SourcePassportRecord.RegistryStatus> plannedSourcePassportStatuses
    ) {
        return plannedSourcePassportStatuses.stream()
                .map(status -> Objects.requireNonNull(status, "sourcePassportStatus must not be null"))
                .anyMatch(SourcePassportRecord.RegistryStatus.REGISTERED::equals);
    }
}
