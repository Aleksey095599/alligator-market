package com.alligator.market.domain.instrument.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record InstrumentSymbol(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .domainCodePattern()
            .build();

    public InstrumentSymbol {
        value = StringValueNormalizer.normalize(value, "instrumentSymbol", NORMALIZATION_OPTIONS);
    }

    public static InstrumentSymbol of(String raw) {
        return new InstrumentSymbol(raw);
    }
}
