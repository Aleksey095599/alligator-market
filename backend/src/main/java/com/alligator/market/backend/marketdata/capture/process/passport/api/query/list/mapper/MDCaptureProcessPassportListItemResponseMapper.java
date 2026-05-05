package com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.mapper;

import com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.dto.MDCaptureProcessPassportListItemResponse;
import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

import java.util.Objects;

/**
 * Маппер паспорта процесса захвата и {@link MDCaptureProcessPassportListItemResponse}.
 */
public final class MDCaptureProcessPassportListItemResponseMapper {

    private MDCaptureProcessPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MDCaptureProcessPassportListItemResponse toResponse(
            MDCaptureProcessCode captureProcessCode,
            MDCaptureProcessPassport passport
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");

        return new MDCaptureProcessPassportListItemResponse(
                captureProcessCode.value(),
                passport.displayName().value()
        );
    }
}
