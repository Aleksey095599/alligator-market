package com.alligator.market.domain.marketdata.tick.level.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Instrument identifier as received from an external market data source, before internal code mapping.
 */
public record SourceInstrumentCode(
        String value
) {

    private static final int MAX_LENGTH = 128;
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
