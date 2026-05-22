package com.alligator.market.domain.capturer.passport.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum StoredCapturerPassportRegistryStatus {
    REGISTERED,
    RETIRED;

    private static final int MAX_CODE_LENGTH = 10;

    StoredCapturerPassportRegistryStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredCapturerPassportRegistryStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("StoredCapturerPassportRegistryStatus", name());
    }
}
