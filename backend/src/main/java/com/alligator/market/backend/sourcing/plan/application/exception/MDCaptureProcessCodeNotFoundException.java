package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: процесс фиксации с указанным кодом не найден.
 */
public final class MDCaptureProcessCodeNotFoundException extends IllegalArgumentException {

    public MDCaptureProcessCodeNotFoundException(MDCaptureProcessCode captureProcessCode) {
        super("Capture process code '" + Objects.requireNonNull(captureProcessCode,
                "captureProcessCode must not be null").value() + "' does not exist");
    }
}
