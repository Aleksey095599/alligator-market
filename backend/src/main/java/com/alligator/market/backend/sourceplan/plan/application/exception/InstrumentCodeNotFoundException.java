package com.alligator.market.backend.sourceplan.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: инструмент с указанным кодом не найден.
 */
public final class InstrumentCodeNotFoundException extends IllegalArgumentException {

    public InstrumentCodeNotFoundException(InstrumentCode instrumentCode) {
        super("Instrument code '" + Objects.requireNonNull(instrumentCode,
                "instrumentCode must not be null").value() + "' does not exist");
    }
}
