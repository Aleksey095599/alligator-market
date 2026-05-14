package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum SourcePlanExecutionStatus {
    EXECUTABLE,
    CAPTURER_RETIRED,
    NO_EXECUTABLE_SOURCES;

    SourcePlanExecutionStatus() {
        DomainCodeFormat.requireValidEnumCode("SourcePlanExecutionStatus", name());
    }
}
