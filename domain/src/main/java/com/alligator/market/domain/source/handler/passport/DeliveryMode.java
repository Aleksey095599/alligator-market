package com.alligator.market.domain.source.handler.passport;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum DeliveryMode {
    PULL,
    PUSH;

    static final int CODE_LENGTH = 4;

    DeliveryMode() {
        if (name().length() != CODE_LENGTH) {
            throw new IllegalStateException(
                    "DeliveryMode code must be exactly " + CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("DeliveryMode", name());
    }

    public String code() {
        return name();
    }
}
