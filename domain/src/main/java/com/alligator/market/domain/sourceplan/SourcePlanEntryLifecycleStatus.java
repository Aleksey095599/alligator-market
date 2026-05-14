package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

/**
 * RETIRED is reversible: sync can reactivate an entry when its referenced passports become active again.
 */
public enum SourcePlanEntryLifecycleStatus {
    ACTIVE,
    RETIRED;

    private static final int MAX_CODE_LENGTH = 7;

    SourcePlanEntryLifecycleStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "SourcePlanEntryLifecycleStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("SourcePlanEntryLifecycleStatus", name());
    }
}
