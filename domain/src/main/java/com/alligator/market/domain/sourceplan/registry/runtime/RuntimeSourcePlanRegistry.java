package com.alligator.market.domain.sourceplan.registry.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanKey;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RuntimeSourcePlanRegistry {

    Optional<SourcePlan> findAvailableByKey(SourcePlanKey key);

    List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode);

    Map<SourcePlanKey, SourcePlan> availablePlansByKey();
}
