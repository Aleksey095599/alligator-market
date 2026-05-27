package com.alligator.market.domain.sourceplan.registry.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SnapshotRuntimeSourcePlanRegistry implements RuntimeSourcePlanRegistry {

    private final Map<SourcePlanKey, SourcePlan> availablePlansByKey;

    private final Map<CapturerCode, List<SourcePlan>> availablePlansByCapturerCode;

    public SnapshotRuntimeSourcePlanRegistry(List<SourcePlan> availablePlans) {
        Objects.requireNonNull(availablePlans, "availablePlans must not be null");

        Map<SourcePlanKey, SourcePlan> plansByKey = new LinkedHashMap<>();
        Map<CapturerCode, List<SourcePlan>> plansByCapturerCode = new LinkedHashMap<>();

        for (SourcePlan plan : availablePlans) {
            SourcePlan planToRegister = Objects.requireNonNull(plan, "plan must not be null");
            SourcePlanKey key = planToRegister.key();

            SourcePlan previous = plansByKey.put(key, planToRegister);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate source plan key detected (capturerCode=%s, instrumentCode=%s)"
                                .formatted(key.capturerCode().value(), key.instrumentCode().value())
                );
            }

            plansByCapturerCode
                    .computeIfAbsent(key.capturerCode(), ignored -> new ArrayList<>())
                    .add(planToRegister);
        }

        this.availablePlansByKey = Collections.unmodifiableMap(plansByKey);
        this.availablePlansByCapturerCode = freezePlansByCapturerCode(plansByCapturerCode);
    }

    @Override
    public Optional<SourcePlan> findAvailableByKey(SourcePlanKey key) {
        Objects.requireNonNull(key, "key must not be null");

        return Optional.ofNullable(availablePlansByKey.get(key));
    }

    @Override
    public List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return availablePlansByCapturerCode.getOrDefault(capturerCode, List.of());
    }

    @Override
    public Map<SourcePlanKey, SourcePlan> availablePlansByKey() {
        return availablePlansByKey;
    }

    private static Map<CapturerCode, List<SourcePlan>> freezePlansByCapturerCode(
            Map<CapturerCode, List<SourcePlan>> plansByCapturerCode
    ) {
        Map<CapturerCode, List<SourcePlan>> frozen = new LinkedHashMap<>();

        for (Map.Entry<CapturerCode, List<SourcePlan>> entry : plansByCapturerCode.entrySet()) {
            frozen.put(entry.getKey(), List.copyOf(entry.getValue()));
        }

        return Collections.unmodifiableMap(frozen);
    }
}
