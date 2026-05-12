package com.alligator.market.backend.sourceplan.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Objects;

public final class SourcePlanAlreadyExistsException extends IllegalStateException {
    public SourcePlanAlreadyExistsException(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        super("Source plan for capturer '" +
                Objects.requireNonNull(capturerCode, "capturerCode must not be null").value() +
                "' and instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' already exists");
    }
}
