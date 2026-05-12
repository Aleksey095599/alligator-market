package com.alligator.market.backend.sourceplan.plan.application.query.common.port;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.List;
import java.util.Optional;

public interface SourcePlanQueryPort {
    Optional<SourcePlanQueryItem> findByMarketDataCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    List<SourcePlanQueryItem> findAll();
}
