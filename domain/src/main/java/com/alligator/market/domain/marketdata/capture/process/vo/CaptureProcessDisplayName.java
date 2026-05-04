package com.alligator.market.domain.marketdata.capture.process.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение отображаемого имени процесса фиксации рыночных данных.
 *
 * @param value непустое имя процесса для интерфейса и диагностики
 */
public record CaptureProcessDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 160;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public CaptureProcessDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static CaptureProcessDisplayName of(String raw) {
        return new CaptureProcessDisplayName(raw);
    }
}
