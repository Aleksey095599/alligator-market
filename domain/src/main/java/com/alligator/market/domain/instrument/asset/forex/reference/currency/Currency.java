package com.alligator.market.domain.instrument.asset.forex.reference.currency;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.shared.vo.StringValueNormalizer;

import java.util.Objects;

/**
 * Currency reference data used by FOREX instruments.
 */
public record Currency(
        CurrencyCode code,
        String name,
        String country,
        int fractionDigits
) {
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_COUNTRY_LENGTH = 50;

    private static final StringValueNormalizer.Options NAME_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_NAME_LENGTH)
            .rejectControlCharacters()
            .build();

    private static final StringValueNormalizer.Options COUNTRY_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_COUNTRY_LENGTH)
            .rejectControlCharacters()
            .build();

    public Currency(CurrencyCode code, String name, String country, int fractionDigits) {
        Objects.requireNonNull(code, "code must not be null");

        if (fractionDigits < 0 || fractionDigits > 10) {
            throw new IllegalArgumentException("fractionDigits must be between 0 and 10");
        }

        this.code = code;
        this.name = StringValueNormalizer.normalize(name, "name", NAME_OPTIONS);
        this.country = StringValueNormalizer.normalize(country, "country", COUNTRY_OPTIONS);
        this.fractionDigits = fractionDigits;
    }
}
