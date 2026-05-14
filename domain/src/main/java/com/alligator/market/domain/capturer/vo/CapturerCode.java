package com.alligator.market.domain.capturer.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record CapturerCode(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .domainCodePattern()
            .build();

    public CapturerCode {
        value = StringValueNormalizer.normalize(value, "capturerCode", NORMALIZATION_OPTIONS);
    }

    public static CapturerCode of(String raw) {
        return new CapturerCode(raw);
    }
}
