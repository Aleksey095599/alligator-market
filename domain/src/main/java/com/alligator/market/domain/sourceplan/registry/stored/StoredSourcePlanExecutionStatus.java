package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredSourcePlanExecutionStatus {
    AVAILABLE,
    CAPTURER_RETIRED,
    NO_AVAILABLE_SOURCES;

    private static final int MAX_CODE_LENGTH = 20;

    StoredSourcePlanExecutionStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredSourcePlanExecutionStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("StoredSourcePlanExecutionStatus", name());
    }
}
