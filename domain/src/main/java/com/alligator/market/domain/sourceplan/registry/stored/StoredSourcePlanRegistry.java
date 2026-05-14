package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanKey;

import java.util.List;
import java.util.Optional;

public interface StoredSourcePlanRegistry {

    Optional<SourcePlan> findExecutableByKey(SourcePlanKey key);

    List<SourcePlan> findExecutableByCapturerCode(CapturerCode capturerCode);

    List<SourcePlan> findAllExecutable();
}
