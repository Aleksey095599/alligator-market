package com.alligator.market.domain.marketdata.collection.process.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение кода процесса сбора рыночных данных.
 *
 * @param value нормализованный код процесса сбора рыночных данных
 */
public record MarketDataCollectionProcessCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 128;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_.-]+")
            .build();

    public MarketDataCollectionProcessCode {
        value = StringValueNormalizer.normalize(value, "collectionProcessCode", NORMALIZATION_OPTIONS);
    }

    public static MarketDataCollectionProcessCode of(String raw) {
        return new MarketDataCollectionProcessCode(raw);
    }
}
