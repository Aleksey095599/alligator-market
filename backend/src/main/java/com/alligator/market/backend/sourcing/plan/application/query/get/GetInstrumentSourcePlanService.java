package com.alligator.market.backend.sourcing.plan.application.query.get;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;

import java.util.Objects;

/**
 * Сервис чтения плана источников рыночных данных для инструмента.
 */
public final class GetInstrumentSourcePlanService {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    public GetInstrumentSourcePlanService(InstrumentSourcePlanRepository instrumentSourcePlanRepository) {
        this.instrumentSourcePlanRepository = Objects.requireNonNull(
                instrumentSourcePlanRepository,
                "instrumentSourcePlanRepository must not be null"
        );
    }

    /**
     * Возвращает план источников для инструмента.
     */
    public InstrumentSourcePlan get(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return instrumentSourcePlanRepository.findByInstrumentCode(instrumentCode)
                .orElseThrow(() -> new InstrumentSourcePlanNotFoundException(instrumentCode));
    }
}
