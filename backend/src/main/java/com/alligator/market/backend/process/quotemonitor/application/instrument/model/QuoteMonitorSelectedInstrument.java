package com.alligator.market.backend.process.quotemonitor.application.instrument.model;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus;

import java.util.Objects;

public record QuoteMonitorSelectedInstrument(
        InstrumentCode instrumentCode,
        StoredSourcePlanExecutionStatus sourcePlanStatus
) {
    public QuoteMonitorSelectedInstrument {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourcePlanStatus, "sourcePlanStatus must not be null");
    }
}
