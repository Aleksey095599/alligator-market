package com.alligator.market.domain.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record SourceCode(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .domainCodePattern()
            .build();

    public SourceCode {
        value = StringValueNormalizer.normalize(value, "sourceCode", NORMALIZATION_OPTIONS);
    }

    public static SourceCode of(String raw) {
        return new SourceCode(raw);
    }
}
