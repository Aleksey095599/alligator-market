package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента уже существует.
 */
public final class MarketDataSourcePlanAlreadyExistsException extends IllegalStateException {

    public MarketDataSourcePlanAlreadyExistsException(InstrumentCode instrumentCode) {
        super("Market data source plan for instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' already exists");
    }
}
