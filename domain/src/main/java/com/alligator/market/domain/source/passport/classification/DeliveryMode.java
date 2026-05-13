package com.alligator.market.domain.source.passport.classification;

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
    }

    public String code() {
        return name();
    }
}
