package com.alligator.market.domain.marketdata.tick.level.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

public record SourceInstrumentCode(
        String value
) {

    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public SourceInstrumentCode {
        value = StringValueNormalizer.normalize(value, "sourceInstrumentCode", NORMALIZATION_OPTIONS);
    }

    public static SourceInstrumentCode of(String raw) {
        return new SourceInstrumentCode(raw);
    }
}
