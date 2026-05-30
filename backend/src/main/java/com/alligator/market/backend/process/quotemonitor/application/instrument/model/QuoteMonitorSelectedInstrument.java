package com.alligator.market.backend.process.quotemonitor.application.instrument.model;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlan;

import java.util.Objects;

public record QuoteMonitorSelectedInstrument(
        InstrumentCode instrumentCode,
        StoredSourcePlan.ExecutionStatus sourcePlanStatus
) {
    public QuoteMonitorSelectedInstrument {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourcePlanStatus, "sourcePlanStatus must not be null");
    }
}
