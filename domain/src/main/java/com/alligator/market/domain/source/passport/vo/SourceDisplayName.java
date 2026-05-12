package com.alligator.market.domain.source.passport.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record SourceDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 100;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public SourceDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static SourceDisplayName of(String raw) {
        return new SourceDisplayName(raw);
    }
}
