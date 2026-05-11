package com.alligator.market.backend.sourceplan.plan.application.query.common.model;

import com.alligator.market.backend.sourceplan.plan.persistence.model.StoredSourcePlanExecutionStatus;

import java.util.List;
import java.util.Objects;

public record SourcePlanQueryItem(
        String capturerCode,
        String capturerLifecycleStatus,
        StoredSourcePlanExecutionStatus planExecutionStatus,
        String instrumentCode,
        List<MarketDataSourceQueryItem> sources
) {
    public SourcePlanQueryItem {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(capturerLifecycleStatus, "capturerLifecycleStatus must not be null");
        Objects.requireNonNull(planExecutionStatus, "planExecutionStatus must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");
        sources = List.copyOf(sources);
    }
}
