package com.alligator.market.domain.source.passport.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение отображаемого имени провайдера рыночных данных.
 *
 * @param value непустое имя провайдера для интерфейса и диагностики
 */
public record ProviderDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public ProviderDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static ProviderDisplayName of(String raw) {
        return new ProviderDisplayName(raw);
    }
}
