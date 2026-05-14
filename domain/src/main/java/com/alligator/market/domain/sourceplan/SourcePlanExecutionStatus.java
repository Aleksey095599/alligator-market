package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum SourcePlanExecutionStatus {
    EXECUTABLE,
    CAPTURER_RETIRED,
    NO_EXECUTABLE_SOURCES;

    private static final int MAX_CODE_LENGTH = 21;

    SourcePlanExecutionStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "SourcePlanExecutionStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("SourcePlanExecutionStatus", name());
    }
}
