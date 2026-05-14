package com.alligator.market.domain.source.passport.classification;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum AccessMethod {
    API_POLL,
    WEBSOCKET,
    FIX_PROTOCOL;

    static final int MAX_CODE_LENGTH = 20;

    AccessMethod() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "AccessMethod code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("AccessMethod", name());
    }

    public String code() {
        return name();
    }
}
