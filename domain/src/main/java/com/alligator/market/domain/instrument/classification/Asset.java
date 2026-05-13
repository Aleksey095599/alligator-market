package com.alligator.market.domain.instrument.classification;

public enum Asset {
    COMMODITY,
    EQUITY,
    FOREX;

    private static final int MAX_CODE_LENGTH = 20;

    Asset() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "Asset code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
    }

    public String code() {
        return name();
    }
}
