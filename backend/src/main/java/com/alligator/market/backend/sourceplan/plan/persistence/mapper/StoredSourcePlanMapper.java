package com.alligator.market.backend.sourceplan.plan.persistence.mapper;

import com.alligator.market.backend.sourceplan.plan.persistence.model.StoredSourcePlan;
import com.alligator.market.backend.sourceplan.plan.persistence.model.StoredSourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.sourceplan.plan.persistence.model.SourcePlanEntryLifecycleStatus.ACTIVE;

public final class StoredSourcePlanMapper {

    public StoredSourcePlan toActiveStored(SourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        List<StoredSourcePlanEntry> entries = new ArrayList<>(plan.entries().size());
        for (SourcePlanEntry entry : plan.entries()) {
            entries.add(new StoredSourcePlanEntry(
                    plan.capturerCode(),
                    plan.instrumentCode(),
                    entry,
                    ACTIVE
            ));
        }

        return new StoredSourcePlan(
                plan.capturerCode(),
                plan.instrumentCode(),
                entries
        );
    }
}
