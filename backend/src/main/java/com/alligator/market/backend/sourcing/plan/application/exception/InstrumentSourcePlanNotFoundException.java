package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента не найден.
 */
public final class InstrumentSourcePlanNotFoundException extends IllegalStateException {

    public InstrumentSourcePlanNotFoundException(InstrumentCode instrumentCode) {
        super("Instrument source plan for instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' does not exist");
    }
}
