package com.alligator.market.domain.marketdata.capture.process.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение кода процесса фиксации рыночных данных.
 *
 * @param value нормализованный код процесса фиксации рыночных данных
 */
public record CaptureProcessCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 128;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_.-]+")
            .build();

    public CaptureProcessCode {
        value = StringValueNormalizer.normalize(value, "captureProcessCode", NORMALIZATION_OPTIONS);
    }

    public static CaptureProcessCode of(String raw) {
        return new CaptureProcessCode(raw);
    }
}
