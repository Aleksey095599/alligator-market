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

        // Условно удаляем root-plan и сигнализируем, если его не было
        boolean deleted = instrumentSourcePlanRepository.deleteIfExistsByInstrumentCode(instrumentCode);
        if (!deleted) {
            throw new InstrumentSourcePlanNotFoundException(instrumentCode);
        }
    }
}
