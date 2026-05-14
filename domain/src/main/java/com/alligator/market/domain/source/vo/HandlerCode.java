package com.alligator.market.domain.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record HandlerCode(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .domainCodePattern()
            .build();

    public HandlerCode {
        value = StringValueNormalizer.normalize(value, "handlerCode", NORMALIZATION_OPTIONS);
    }

    public static HandlerCode of(String raw) {
        return new HandlerCode(raw);
    }
}
