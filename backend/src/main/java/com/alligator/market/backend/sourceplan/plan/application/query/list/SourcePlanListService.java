package com.alligator.market.backend.sourceplan.plan.application.query.list;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;

import java.util.List;
import java.util.Objects;

public final class SourcePlanListService {

    private final SourcePlanQueryPort sourcePlanQueryPort;

    public SourcePlanListService(SourcePlanQueryPort sourcePlanQueryPort) {
        this.sourcePlanQueryPort = Objects.requireNonNull(
                sourcePlanQueryPort,
                "sourcePlanQueryPort must not be null"
        );
    }

    public List<SourcePlanQueryItem> list() {
        return sourcePlanQueryPort.findAll();
    }
}
