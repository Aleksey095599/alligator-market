package com.alligator.market.backend.sourceplan.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента уже существует.
 */
public final class SourcePlanAlreadyExistsException extends IllegalStateException {

    public SourcePlanAlreadyExistsException(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        super("Source plan for capturer '" +
                Objects.requireNonNull(capturerCode, "capturerCode must not be null").value() +
                "' and instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' already exists");
    }
}
