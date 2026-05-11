package com.alligator.market.backend.sourceplan.plan.persistence.model;

import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;
import java.util.Objects;

public record StoredSourcePlan(
        MarketDataCapturerCode capturerCode,
        InstrumentCode instrumentCode,
        SourcePlanExecutionStatus executionStatus,
        List<StoredSourcePlanEntry> entries
) {
    public StoredSourcePlan {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(executionStatus, "executionStatus must not be null");
        Objects.requireNonNull(entries, "entries must not be null");

        if (entries.isEmpty()) {
            throw new IllegalArgumentException("entries must not be empty");
        }

        entries = List.copyOf(entries);
    }
}
