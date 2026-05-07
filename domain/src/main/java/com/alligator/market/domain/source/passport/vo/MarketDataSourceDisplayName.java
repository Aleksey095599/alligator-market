package com.alligator.market.domain.source.passport.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Display name of a market data source.
 *
 * @param value the non-empty source name used in UI and diagnostics
 */
public record MarketDataSourceDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public MarketDataSourceDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static MarketDataSourceDisplayName of(String raw) {
        return new MarketDataSourceDisplayName(raw);
    }
}
