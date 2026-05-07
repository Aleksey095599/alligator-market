package com.alligator.market.domain.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение кода провайдера рыночных данных.
 *
 * @param value нормализованный код провайдера
 */
public record ProviderCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_.-]+")
            .build();

    public ProviderCode {
        value = StringValueNormalizer.normalize(value, "providerCode", NORMALIZATION_OPTIONS);
    }

    public static ProviderCode of(String raw) {
        return new ProviderCode(raw);
    }
}
