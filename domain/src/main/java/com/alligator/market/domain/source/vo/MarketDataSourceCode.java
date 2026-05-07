package com.alligator.market.domain.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Code of a market data source.
 *
 * @param value the normalized source code
 */
public record MarketDataSourceCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_.-]+")
            .build();

    public MarketDataSourceCode {
        value = StringValueNormalizer.normalize(value, "sourceCode", NORMALIZATION_OPTIONS);
    }

    public static MarketDataSourceCode of(String raw) {
        return new MarketDataSourceCode(raw);
    }
}
