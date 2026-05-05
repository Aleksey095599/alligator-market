package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента не найден.
 */
public final class MarketDataSourcePlanNotFoundException extends IllegalStateException {

    public MarketDataSourcePlanNotFoundException(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source plan for capture process '" +
                Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null").value() +
                "' and instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' does not exist");
    }
}
