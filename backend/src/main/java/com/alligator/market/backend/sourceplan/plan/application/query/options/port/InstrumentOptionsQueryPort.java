package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

public interface InstrumentOptionsQueryPort {
    List<InstrumentCode> findAllInstrumentCodes();
}
