package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.FxSpotUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.port.SourcePlanExistenceQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Проверка использования FX_SPOT инструмента для contributor: sourcing/source_plan.
 */
public final class SourcePlanFxSpotUsageContributor implements FxSpotUsageContributor {

    private final SourcePlanExistenceQueryPort sourcePlanExistenceQueryPort;

    public SourcePlanFxSpotUsageContributor(SourcePlanExistenceQueryPort sourcePlanExistenceQueryPort) {
        this.sourcePlanExistenceQueryPort = Objects.requireNonNull(sourcePlanExistenceQueryPort,
                "sourcePlanExistenceQueryPort must not be null");
    }

    @Override
    public boolean isUsed(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return sourcePlanExistenceQueryPort.existsByInstrumentCode(instrumentCode);
    }
}
