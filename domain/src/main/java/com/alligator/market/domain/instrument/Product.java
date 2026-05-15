package com.alligator.market.domain.instrument;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum Product {
    FORWARD,
    SPOT,
    SWAP;

    private static final int MAX_CODE_LENGTH = 10;

    Product() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "Product code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("Product", name());
    }

    public String code() {
        return name();
    }
}
