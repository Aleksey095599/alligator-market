package com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.mapper;

import com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.dto.MarketDataCaptureProcessPassportListItemResponse;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.Objects;

/**
 * Маппер паспорта процесса захвата и {@link MarketDataCaptureProcessPassportListItemResponse}.
 */
public final class MarketDataCaptureProcessPassportListItemResponseMapper {

    private MarketDataCaptureProcessPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static MarketDataCaptureProcessPassportListItemResponse toResponse(
            MarketDataCaptureProcessCode captureProcessCode,
            MarketDataCaptureProcessPassport passport
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");

        return new MarketDataCaptureProcessPassportListItemResponse(
                captureProcessCode.value(),
                passport.displayName().value()
        );
    }
}
