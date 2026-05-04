package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента уже существует.
 */
public final class MarketDataSourcePlanAlreadyExistsException extends IllegalStateException {

    public MarketDataSourcePlanAlreadyExistsException(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source plan for capture process '" +
                Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null").value() +
                "' and instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' already exists");
    }
}
