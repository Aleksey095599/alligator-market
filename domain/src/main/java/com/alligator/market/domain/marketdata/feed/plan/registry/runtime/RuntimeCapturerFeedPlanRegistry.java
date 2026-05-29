package com.alligator.market.domain.marketdata.feed.plan.registry.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.plan.CapturerFeedPlan;

import java.util.List;
import java.util.Optional;

public interface RuntimeCapturerFeedPlanRegistry {

    Optional<CapturerFeedPlan> findAvailableByCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    List<CapturerFeedPlan> findAvailableByCapturerCode(CapturerCode capturerCode);

    List<CapturerFeedPlan> findAllAvailable();
}
