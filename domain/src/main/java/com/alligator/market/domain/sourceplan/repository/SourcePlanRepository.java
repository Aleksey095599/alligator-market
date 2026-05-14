package com.alligator.market.domain.sourceplan.repository;

import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanKey;

import java.util.List;
import java.util.Optional;

public interface SourcePlanRepository {

    Optional<SourcePlan> findByKey(SourcePlanKey key);

    List<SourcePlan> findAll();

    boolean createIfAbsent(SourcePlan plan);

    boolean replaceIfExists(SourcePlan plan);

    boolean deleteIfExistsByKey(SourcePlanKey key);
}
