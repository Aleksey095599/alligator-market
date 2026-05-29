package com.alligator.market.domain.marketdata.feed.plan.registry.sync;

import com.alligator.market.domain.marketdata.feed.plan.registry.runtime.RuntimeCapturerFeedPlanRegistry;

public interface RuntimeCapturerFeedPlanRegistryPublisher {

    void replaceWith(RuntimeCapturerFeedPlanRegistry registry);
}
