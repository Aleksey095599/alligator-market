package com.alligator.market.domain.capturer.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record MarketDataCapturerDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 160;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public MarketDataCapturerDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static MarketDataCapturerDisplayName of(String raw) {
        return new MarketDataCapturerDisplayName(raw);
    }
}
