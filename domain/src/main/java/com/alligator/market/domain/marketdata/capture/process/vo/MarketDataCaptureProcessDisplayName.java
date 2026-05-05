package com.alligator.market.domain.marketdata.capture.process.vo;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;

/**
 * Объект-значение отображаемого имени процесса захвата рыночных данных.
 *
 * @param value непустое имя процесса для интерфейса и диагностики
 */
public record MarketDataCaptureProcessDisplayName(
        String value
) {

    private static final int MAX_LENGTH = 160;
    private static final StringValueNormalizer.Options NORMALIZATION_OPTIONS = StringValueNormalizer.options()
            .maxLength(MAX_LENGTH)
            .rejectControlCharacters()
            .build();

    public MarketDataCaptureProcessDisplayName {
        value = StringValueNormalizer.normalize(value, "displayName", NORMALIZATION_OPTIONS);
    }

    public static MarketDataCaptureProcessDisplayName of(String raw) {
        return new MarketDataCaptureProcessDisplayName(raw);
    }
}
