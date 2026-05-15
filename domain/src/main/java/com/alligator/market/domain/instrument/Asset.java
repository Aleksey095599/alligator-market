package com.alligator.market.domain.instrument;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum Asset {
    COMMODITY,
    EQUITY,
    FOREX;

    private static final int MAX_CODE_LENGTH = 10;

    Asset() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "Asset code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("Asset", name());
    }

    public String code() {
        return name();
    }
}
