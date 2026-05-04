package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: процесс фиксации с указанным кодом не найден.
 */
public final class CaptureProcessCodeNotFoundException extends IllegalArgumentException {

    public CaptureProcessCodeNotFoundException(CaptureProcessCode captureProcessCode) {
        super("Capture process code '" + Objects.requireNonNull(captureProcessCode,
                "captureProcessCode must not be null").value() + "' does not exist");
    }
}
