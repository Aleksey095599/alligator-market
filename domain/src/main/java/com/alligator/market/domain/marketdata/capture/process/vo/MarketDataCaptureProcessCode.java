package com.alligator.market.domain.marketdata.capture.process.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение кода процесса захвата рыночных данных.
 *
 * @param value нормализованный код процесса
 */
public record MarketDataCaptureProcessCode(
        String value
) {

    private static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 128;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .uppercase()
            .maxLength(MAX_LENGTH)
            .pattern(PATTERN, "[A-Z0-9_.-]+")
            .build();

    public MarketDataCaptureProcessCode {
        value = StringValueNormalizer.normalize(value, "captureProcessCode", NORMALIZATION_OPTIONS);
    }

    public static MarketDataCaptureProcessCode of(String raw) {
        return new MarketDataCaptureProcessCode(raw);
    }
}
