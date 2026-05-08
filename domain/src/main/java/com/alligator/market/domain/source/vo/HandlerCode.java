package com.alligator.market.domain.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Value object for a source handler code.
 *
 * @param value normalized handler code
 */
public record HandlerCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_]+$";
    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_]+")
            .build();

    public HandlerCode {
        value = StringValueNormalizer.normalize(value, "handlerCode", NORMALIZATION_OPTIONS);
    }

    public static HandlerCode of(String raw) {
        return new HandlerCode(raw);
    }
}
