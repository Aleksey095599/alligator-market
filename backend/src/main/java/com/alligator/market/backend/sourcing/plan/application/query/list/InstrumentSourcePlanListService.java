package com.alligator.market.backend.sourcing.plan.application.query.list;

import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;

import java.util.List;
import java.util.Objects;

/**
 * Сервис чтения всех планов источников рыночных данных.
 */
public final class InstrumentSourcePlanListService {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    public InstrumentSourcePlanListService(InstrumentSourcePlanRepository instrumentSourcePlanRepository) {
        this.instrumentSourcePlanRepository = Objects.requireNonNull(
                instrumentSourcePlanRepository,
                "instrumentSourcePlanRepository must not be null"
        );
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    public List<InstrumentSourcePlan> list() {
        return instrumentSourcePlanRepository.findAll();
    }
}
