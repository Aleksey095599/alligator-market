package com.alligator.market.domain.capturer.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Value object for a market data capturer code.
 *
 * @param value normalized capturer code
 */
public record MarketDataCapturerCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 128;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_.-]+")
            .build();

    public MarketDataCapturerCode {
        value = StringValueNormalizer.normalize(value, "capturerCode", NORMALIZATION_OPTIONS);
    }

    public static MarketDataCapturerCode of(String raw) {
        return new MarketDataCapturerCode(raw);
    }
}
