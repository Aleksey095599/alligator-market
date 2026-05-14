package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredSourcePlanExecutionStatus {
    EXECUTABLE,
    CAPTURER_RETIRED,
    NO_EXECUTABLE_SOURCES;

    private static final int MAX_CODE_LENGTH = 21;

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
