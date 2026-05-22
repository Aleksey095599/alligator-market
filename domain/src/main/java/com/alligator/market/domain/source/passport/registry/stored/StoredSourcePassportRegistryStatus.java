package com.alligator.market.domain.source.passport.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredSourcePassportRegistryStatus {
    REGISTERED,
    RETIRED;

    private static final int MAX_CODE_LENGTH = 10;

    StoredSourcePassportRegistryStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredSourcePassportRegistryStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("StoredSourcePassportRegistryStatus", name());
    }
}
