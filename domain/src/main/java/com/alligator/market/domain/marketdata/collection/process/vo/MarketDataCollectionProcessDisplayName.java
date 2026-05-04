package com.alligator.market.domain.marketdata.collection.process.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение отображаемого имени процесса сбора рыночных данных.
 *
 * @param value непустое имя процесса для интерфейса и диагностики
 */
public record MarketDataCollectionProcessDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 160;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public MarketDataCollectionProcessDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static MarketDataCollectionProcessDisplayName of(String raw) {
        return new MarketDataCollectionProcessDisplayName(raw);
    }
}
