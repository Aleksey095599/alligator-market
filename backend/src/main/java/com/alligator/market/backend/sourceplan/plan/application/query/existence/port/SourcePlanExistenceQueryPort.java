package com.alligator.market.backend.sourceplan.plan.application.query.existence.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

public interface SourcePlanExistenceQueryPort {
    boolean existsByInstrumentCode(InstrumentCode instrumentCode);
}
