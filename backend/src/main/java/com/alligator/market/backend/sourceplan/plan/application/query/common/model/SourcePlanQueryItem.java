package com.alligator.market.backend.sourceplan.plan.application.query.common.model;

import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlan;

import java.util.List;
import java.util.Objects;

public record SourcePlanQueryItem(
        String capturerCode,
        CapturerPassportRecord.RegistryStatus capturerRegistryStatus,
        StoredSourcePlan.ExecutionStatus planExecutionStatus,
        String instrumentCode,
        List<SourceQueryItem> sources
) {
    public SourcePlanQueryItem {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(capturerRegistryStatus, "capturerRegistryStatus must not be null");
        Objects.requireNonNull(planExecutionStatus, "planExecutionStatus must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");
        sources = List.copyOf(sources);
    }
}
