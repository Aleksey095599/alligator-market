package com.alligator.market.domain.instrument.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение уникального внутреннего кода инструмента.
 *
 * @param value нормализованный код инструмента
 */
public record InstrumentCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_]+$";
    private static final int MAX_LENGTH = 50;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_]+")
            .build();

    public InstrumentCode {
        value = StringValueNormalizer.normalize(value, "instrumentCode", NORMALIZATION_OPTIONS);
    }

    public static InstrumentCode of(String raw) {
        return new InstrumentCode(raw);
    }
}
