package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;

import java.util.List;
import java.util.Optional;

public interface StoredSourcePlanRegistry {

    Optional<SourcePlan> findAvailableByKey(SourcePlanKey key);

    List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode);

    List<SourcePlan> findAllAvailable();
}
