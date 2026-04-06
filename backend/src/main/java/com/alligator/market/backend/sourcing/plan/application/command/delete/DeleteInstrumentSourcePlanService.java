package com.alligator.market.backend.sourcing.plan.application.command.delete;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;

import java.util.Objects;

/**
 * Сервис удаления плана источников рыночных данных для инструмента.
 */
public final class DeleteInstrumentSourcePlanService {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    public DeleteInstrumentSourcePlanService(
            InstrumentSourcePlanRepository instrumentSourcePlanRepository
    ) {
        this.instrumentSourcePlanRepository = Objects.requireNonNull(
                instrumentSourcePlanRepository,
                "instrumentSourcePlanRepository must not be null"
        );
    }

    /**
     * Удаляет существующий план источников для инструмента.
     */
    public void delete(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Проверяем, что удаляемый план существует
        ensurePlanExists(instrumentCode);

        instrumentSourcePlanRepository.deleteByInstrumentCode(instrumentCode);
    }

    /**
     * Проверяет существование плана перед удалением.
     */
    private void ensurePlanExists(InstrumentCode instrumentCode) {
        if (instrumentSourcePlanRepository.findByInstrumentCode(instrumentCode).isEmpty()) {
            throw new InstrumentSourcePlanNotFoundException(instrumentCode);
        }
    }
}
