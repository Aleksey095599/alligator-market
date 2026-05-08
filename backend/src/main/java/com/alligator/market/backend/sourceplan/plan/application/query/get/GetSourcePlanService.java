package com.alligator.market.backend.sourceplan.plan.application.query.get;

import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Objects;

public final class GetSourcePlanService {
    private final SourcePlanQueryPort sourcePlanQueryPort;

    public GetSourcePlanService(SourcePlanQueryPort sourcePlanQueryPort) {
        this.sourcePlanQueryPort = Objects.requireNonNull(
                sourcePlanQueryPort,
                "sourcePlanQueryPort must not be null"
        );
    }

    public SourcePlanQueryItem get(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return sourcePlanQueryPort
                .findByMarketDataCapturerCodeAndInstrumentCode(capturerCode, instrumentCode)
                .orElseThrow(() -> new SourcePlanNotFoundException(capturerCode, instrumentCode));
    }
}
