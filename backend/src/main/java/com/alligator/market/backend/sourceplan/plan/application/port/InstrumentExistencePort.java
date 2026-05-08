package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

public interface InstrumentExistencePort {
    boolean existsByCode(InstrumentCode code);
}
