package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: процесс захвата с указанным кодом не найден.
 */
public final class MarketDataCaptureProcessCodeNotFoundException extends IllegalArgumentException {

    public MarketDataCaptureProcessCodeNotFoundException(MarketDataCaptureProcessCode captureProcessCode) {
        super("Capture process code '" + Objects.requireNonNull(captureProcessCode,
                "captureProcessCode must not be null").value() + "' does not exist");
    }
}
