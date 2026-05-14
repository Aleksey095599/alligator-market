package com.alligator.market.backend.sourceplan.plan.persistence.model;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.sourceplan.SourcePlanEntryLifecycleStatus;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.Objects;

public record StoredSourcePlanEntry(
        CapturerCode capturerCode,
        InstrumentCode instrumentCode,
        SourcePlanEntry entry,
        SourcePlanEntryLifecycleStatus lifecycleStatus
) {
    public StoredSourcePlanEntry {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(entry, "entry must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
