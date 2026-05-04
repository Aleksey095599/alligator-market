package com.alligator.market.backend.marketdata.capture.process.api.passport.query.list.mapper;

import com.alligator.market.backend.marketdata.capture.process.api.passport.query.list.dto.CaptureProcessPassportListItemResponse;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;

/**
 * Маппер паспорта процесса фиксации и {@link CaptureProcessPassportListItemResponse}.
 */
public final class CaptureProcessPassportListItemResponseMapper {

    private CaptureProcessPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CaptureProcessPassportListItemResponse toResponse(
            CaptureProcessCode captureProcessCode,
            CaptureProcessPassport passport
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");

        return new CaptureProcessPassportListItemResponse(
                captureProcessCode.value(),
                passport.displayName().value()
        );
    }
}
