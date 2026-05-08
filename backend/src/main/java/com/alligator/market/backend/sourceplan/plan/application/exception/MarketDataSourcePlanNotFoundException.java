package com.alligator.market.backend.sourceplan.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента не найден.
 */
public final class MarketDataSourcePlanNotFoundException extends IllegalStateException {

    public MarketDataSourcePlanNotFoundException(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source plan for capturer '" +
                Objects.requireNonNull(capturerCode, "capturerCode must not be null").value() +
                "' and instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' does not exist");
    }
}
