package com.alligator.market.domain.marketdata.feed.plan.registry.stored.status;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredCapturerFeedPlanStatus {
    AVAILABLE,
    CAPTURER_RETIRED,
    NO_AVAILABLE_SOURCES;

    private static final int MAX_CODE_LENGTH = 20;

    StoredCapturerFeedPlanStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredCapturerFeedPlanStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("StoredCapturerFeedPlanStatus", name());
    }
}
