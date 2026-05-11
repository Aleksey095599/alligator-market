package com.alligator.market.backend.sourceplan.plan.persistence.model;

/**
 * RETIRED is reversible: sync can reactivate an entry when its referenced passports become active again.
 */
public enum StoredSourcePlanEntryLifecycleStatus {
    ACTIVE,
    RETIRED
}
