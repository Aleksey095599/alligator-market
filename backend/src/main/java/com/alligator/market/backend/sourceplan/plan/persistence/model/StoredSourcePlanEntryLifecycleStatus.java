package com.alligator.market.backend.sourceplan.plan.persistence.model;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

/**
 * RETIRED is reversible: sync can reactivate an entry when its referenced passports become active again.
 */
public enum StoredSourcePlanEntryLifecycleStatus {
    ACTIVE,
    RETIRED;

    StoredSourcePlanEntryLifecycleStatus() {
        DomainCodeFormat.requireValidEnumCode("StoredSourcePlanEntryLifecycleStatus", name());
    }
}
