package com.alligator.market.domain.marketdata.feed.plan.registry.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.plan.CapturerFeedPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SnapshotRuntimeCapturerFeedPlanRegistry implements RuntimeCapturerFeedPlanRegistry {

    private final List<CapturerFeedPlan> availablePlans;

    private final Map<CapturerFeedPlanLookupKey, CapturerFeedPlan> availablePlansByLookupKey;

    private final Map<CapturerCode, List<CapturerFeedPlan>> availablePlansByCapturerCode;

    public SnapshotRuntimeCapturerFeedPlanRegistry(List<CapturerFeedPlan> availablePlans) {
        Objects.requireNonNull(availablePlans, "availablePlans must not be null");

        List<CapturerFeedPlan> plans = new ArrayList<>(availablePlans.size());
        Map<CapturerFeedPlanLookupKey, CapturerFeedPlan> plansByLookupKey = new LinkedHashMap<>();
        Map<CapturerCode, List<CapturerFeedPlan>> plansByCapturerCode = new LinkedHashMap<>();

        for (CapturerFeedPlan plan : availablePlans) {
            CapturerFeedPlan planToRegister = Objects.requireNonNull(plan, "plan must not be null");
            CapturerFeedPlanLookupKey lookupKey = new CapturerFeedPlanLookupKey(
                    planToRegister.capturerCode(),
                    planToRegister.instrumentCode()
            );

            CapturerFeedPlan previous = plansByLookupKey.put(lookupKey, planToRegister);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate capturer feed plan detected (capturerCode=%s, instrumentCode=%s)"
                                .formatted(
                                        lookupKey.capturerCode().value(),
                                        lookupKey.instrumentCode().value()
                                )
                );
            }

            plans.add(planToRegister);
            plansByCapturerCode
                    .computeIfAbsent(lookupKey.capturerCode(), ignored -> new ArrayList<>())
                    .add(planToRegister);
        }

        this.availablePlans = List.copyOf(plans);
        this.availablePlansByLookupKey = Collections.unmodifiableMap(plansByLookupKey);
        this.availablePlansByCapturerCode = freezePlansByCapturerCode(plansByCapturerCode);
    }

    @Override
    public Optional<CapturerFeedPlan> findAvailableByCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        CapturerFeedPlanLookupKey lookupKey = new CapturerFeedPlanLookupKey(
                Objects.requireNonNull(capturerCode, "capturerCode must not be null"),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null")
        );

        return Optional.ofNullable(availablePlansByLookupKey.get(lookupKey));
    }

    @Override
    public List<CapturerFeedPlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return availablePlansByCapturerCode.getOrDefault(capturerCode, List.of());
    }

    @Override
    public List<CapturerFeedPlan> findAllAvailable() {
        return availablePlans;
    }

    private static Map<CapturerCode, List<CapturerFeedPlan>> freezePlansByCapturerCode(
            Map<CapturerCode, List<CapturerFeedPlan>> plansByCapturerCode
    ) {
        Map<CapturerCode, List<CapturerFeedPlan>> frozen = new LinkedHashMap<>();

        for (Map.Entry<CapturerCode, List<CapturerFeedPlan>> entry : plansByCapturerCode.entrySet()) {
            frozen.put(entry.getKey(), List.copyOf(entry.getValue()));
        }

        return Collections.unmodifiableMap(frozen);
    }

    private record CapturerFeedPlanLookupKey(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
    }
}
