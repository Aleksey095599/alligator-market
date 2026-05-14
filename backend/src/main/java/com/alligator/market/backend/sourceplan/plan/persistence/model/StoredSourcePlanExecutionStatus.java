package com.alligator.market.backend.sourceplan.plan.persistence.model;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredSourcePlanExecutionStatus {
    EXECUTABLE,
    CAPTURER_RETIRED,
    NO_EXECUTABLE_SOURCES;

    StoredSourcePlanExecutionStatus() {
        DomainCodeFormat.requireValidEnumCode("StoredSourcePlanExecutionStatus", name());
    }
}
