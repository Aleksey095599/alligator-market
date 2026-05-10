package com.alligator.market.backend.sourceplan.plan.application.query.common.model;

import java.util.List;
import java.util.Objects;

public enum SourcePlanExecutionStatus {
    EXECUTABLE,
    CAPTURER_RETIRED,
    NO_EXECUTABLE_SOURCES;

    private static final String ACTIVE = "ACTIVE";
    private static final String RETIRED = "RETIRED";

    public static SourcePlanExecutionStatus from(
            String capturerLifecycleStatus,
            List<MarketDataSourceQueryItem> sources
    ) {
        Objects.requireNonNull(capturerLifecycleStatus, "capturerLifecycleStatus must not be null");
        Objects.requireNonNull(sources, "sources must not be null");

        if (RETIRED.equals(capturerLifecycleStatus)) {
            return CAPTURER_RETIRED;
        }

        boolean hasExecutableSources = sources.stream()
                .anyMatch(source -> ACTIVE.equals(source.lifecycleStatus()));

        if (!hasExecutableSources) {
            return NO_EXECUTABLE_SOURCES;
        }

        return EXECUTABLE;
    }
}
