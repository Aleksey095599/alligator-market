package com.alligator.market.domain.instrument.asset.forex.reference.currency;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

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
    public Currency(CurrencyCode code, String name, String country, int fractionDigits) {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(country, "country must not be null");

        final String nName = name.strip();
        final String nCountry = country.strip();
        if (nName.isEmpty()) throw new IllegalArgumentException("name must not be blank");
        if (nCountry.isEmpty()) throw new IllegalArgumentException("country must not be blank");

        if (fractionDigits < 0 || fractionDigits > 10) {
            throw new IllegalArgumentException("fractionDigits must be between 0 and 10");
        }

        this.code = code;
        this.name = nName;
        this.country = nCountry;
        this.fractionDigits = fractionDigits;
    }
}
