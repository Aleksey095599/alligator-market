package com.alligator.market.domain.marketdata.feed.plan.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredCapturerFeedPlanSourceLifecycleStatus {
    AVAILABLE,
    SOURCE_RETIRED;

    private static final int MAX_CODE_LENGTH = 14;

    StoredCapturerFeedPlanSourceLifecycleStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredCapturerFeedPlanSourceLifecycleStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("StoredCapturerFeedPlanSourceLifecycleStatus", name());
    }
}
