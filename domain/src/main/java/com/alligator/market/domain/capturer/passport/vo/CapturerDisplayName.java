package com.alligator.market.domain.capturer.passport.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record CapturerDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 100;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public CapturerDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static CapturerDisplayName of(String raw) {
        return new CapturerDisplayName(raw);
    }
}
