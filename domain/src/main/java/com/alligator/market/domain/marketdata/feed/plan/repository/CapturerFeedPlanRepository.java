package com.alligator.market.domain.marketdata.feed.plan.repository;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.plan.CapturerFeedPlan;

import java.util.List;
import java.util.Optional;

public interface CapturerFeedPlanRepository {

    Optional<CapturerFeedPlan> findByCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    List<CapturerFeedPlan> findAll();

    boolean createIfAbsent(CapturerFeedPlan plan);

    boolean replaceIfExists(CapturerFeedPlan plan);

    boolean deleteIfExistsByCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );
}
