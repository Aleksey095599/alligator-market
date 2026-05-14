package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

/**
 * RETIRED is reversible: sync can reactivate an entry when its referenced passports become active again.
 */
public enum SourcePlanEntryLifecycleStatus {
    ACTIVE,
    RETIRED;

    SourcePlanEntryLifecycleStatus() {
        DomainCodeFormat.requireValidEnumCode("SourcePlanEntryLifecycleStatus", name());
    }
}
