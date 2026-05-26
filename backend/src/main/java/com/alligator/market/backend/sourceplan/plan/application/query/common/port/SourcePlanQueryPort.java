package com.alligator.market.backend.sourceplan.plan.application.query.common.port;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SourcePlanQueryPort {
    Optional<SourcePlanQueryItem> findByMarketDataCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    Map<InstrumentCode, StoredSourcePlanExecutionStatus> findExecutionStatusesByMarketDataCapturerCodeAndInstrumentCodes(
            CapturerCode capturerCode,
            List<InstrumentCode> instrumentCodes
    );

    List<SourcePlanQueryItem> findAll();
}
