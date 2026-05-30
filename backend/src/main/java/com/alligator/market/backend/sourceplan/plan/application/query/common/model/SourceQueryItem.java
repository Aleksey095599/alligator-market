package com.alligator.market.backend.sourceplan.plan.application.query.common.model;

import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlan;

import java.util.Objects;

public record SourceQueryItem(
        String sourceCode,
        int priority,
        StoredSourcePlan.EntryLifecycleStatus lifecycleStatus
) {
    public SourceQueryItem {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
