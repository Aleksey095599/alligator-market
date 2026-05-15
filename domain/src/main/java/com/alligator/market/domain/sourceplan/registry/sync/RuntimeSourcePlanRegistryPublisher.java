package com.alligator.market.domain.sourceplan.registry.sync;

import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;

public interface RuntimeSourcePlanRegistryPublisher {

    void replaceWith(RuntimeSourcePlanRegistry registry);
}
