package com.alligator.market.domain.instrument.classification;

public enum Product {
    FORWARD,
    SPOT,
    SWAP;

    private static final int MAX_CODE_LENGTH = 20;

    Product() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "Product code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
    }

    public String code() {
        return name();
    }
}
