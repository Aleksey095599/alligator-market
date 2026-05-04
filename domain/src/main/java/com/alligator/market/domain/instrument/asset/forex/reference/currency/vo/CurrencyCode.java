package com.alligator.market.domain.instrument.asset.forex.reference.currency.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение кода валюты в формате ISO 4217.
 *
 * @param value нормализованный трехбуквенный код валюты
 */
public record CurrencyCode(
        String value
) {

    private static final String PATTERN = "^[A-Z]{3}$";
    private static final int CODE_LENGTH = 3;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .exactLength(CODE_LENGTH)
            .pattern(PATTERN, "[A-Z]{3}")
            .build();

    public CurrencyCode {
        value = StringValueNormalizer.normalize(value, "currencyCode", NORMALIZATION_OPTIONS);
    }

    public static CurrencyCode of(String raw) {
        return new CurrencyCode(raw);
    }
}
