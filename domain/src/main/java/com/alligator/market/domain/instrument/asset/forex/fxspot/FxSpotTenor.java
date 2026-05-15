package com.alligator.market.domain.instrument.asset.forex.fxspot;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum FxSpotTenor {
    TOD,
    TOM,
    SPOT;

    private static final int MAX_CODE_LENGTH = 4;

    FxSpotTenor() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "FxSpotTenor code must not exceed " + MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("FxSpotTenor", name());
    }

    public String code() {
        return name();
    }
}
