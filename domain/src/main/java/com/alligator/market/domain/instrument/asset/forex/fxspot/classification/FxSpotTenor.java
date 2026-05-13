package com.alligator.market.domain.instrument.asset.forex.fxspot.classification;

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
    }

    public String code() {
        return name();
    }
}
