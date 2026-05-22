package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredSourcePlanEntryLifecycleStatus {
    AVAILABLE,
    SOURCE_RETIRED;

    private static final int MAX_CODE_LENGTH = 14;

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
