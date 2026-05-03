package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента не найден.
 */
public final class MarketDataSourcePlanNotFoundException extends IllegalStateException {

    public MarketDataSourcePlanNotFoundException(InstrumentCode instrumentCode) {
        super("Market data source plan for instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' does not exist");
    }
}
