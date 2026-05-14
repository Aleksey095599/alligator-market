package com.alligator.market.domain.capturer.passport.registry.stored;

public enum StoredCapturerPassportRegistryStatus {
    ACTIVE,
    RETIRED;

    private static final int MAX_CODE_LENGTH = 7;

    StoredCapturerPassportRegistryStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "StoredCapturerPassportRegistryStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
    }
}
