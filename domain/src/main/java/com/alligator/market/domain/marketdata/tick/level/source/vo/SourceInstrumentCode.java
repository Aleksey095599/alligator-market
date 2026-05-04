package com.alligator.market.domain.marketdata.tick.level.source.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Идентификатор инструмента в том виде, в котором он приходит из внешнего
 * источника рыночных данных, до сопоставления с внутренним кодом инструмента
 * приложения.
 *
 * @param value код инструмента в системе источника рыночных данных
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
