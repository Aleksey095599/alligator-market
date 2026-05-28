package com.alligator.market.backend.sourceplan.plan.persistence.model;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.sourceplan.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanEntryLifecycleStatus;

import java.util.Objects;

public record StoredSourcePlanEntry(
        CapturerCode capturerCode,
        InstrumentCode instrumentCode,
        PrioritizedSourceCode prioritizedSourceCode,
        StoredSourcePlanEntryLifecycleStatus lifecycleStatus
) {
    public StoredSourcePlanEntry {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(prioritizedSourceCode, "prioritizedSourceCode must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
