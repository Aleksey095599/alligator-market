package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;

import java.util.Objects;

/**
 * Исключение: план источников для инструмента уже существует.
 */
public final class InstrumentSourcePlanAlreadyExistsException extends IllegalStateException {

    public InstrumentSourcePlanAlreadyExistsException(InstrumentCode instrumentCode) {
        super("Instrument source plan for instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' already exists");
    }
}
