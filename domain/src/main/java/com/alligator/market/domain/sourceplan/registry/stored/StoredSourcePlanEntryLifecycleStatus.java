package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

/**
 * RETIRED is reversible: sync can reactivate an entry when its referenced passports become active again.
 */
public enum StoredSourcePlanEntryLifecycleStatus {
    ACTIVE,
    RETIRED;

    private static final int MAX_CODE_LENGTH = 7;

    StoredSourcePlanEntryLifecycleStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredSourcePlanEntryLifecycleStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("StoredSourcePlanEntryLifecycleStatus", name());
    }
}
