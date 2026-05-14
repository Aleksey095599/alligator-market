package com.alligator.market.domain.marketdata.tick.level.source.type;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum SourceTickType {
    LAST_PRICE,
    TOP_OF_BOOK_QUOTE;

    private static final int MAX_CODE_LENGTH = 20;

    SourceTickType() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "SourceTickType code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("SourceTickType", name());
    }

    public String code() {
        return name();
    }
}
