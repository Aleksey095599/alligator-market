package com.alligator.market.domain.source.passport.classification;

public enum DeliveryMode {
    PULL,
    PUSH;

    static final int MAX_CODE_LENGTH = 20;

    DeliveryMode() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "DeliveryMode code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
    }

    public String code() {
        return name();
    }
}
