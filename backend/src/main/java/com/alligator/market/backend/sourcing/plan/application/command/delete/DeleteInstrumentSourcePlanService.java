package com.alligator.market.backend.sourcing.plan.application.command.delete;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис удаления плана источников рыночных данных для инструмента.
 */
@Slf4j
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
            log.warn("Instrument source plan was not found and was not deleted: instrumentCode={}", instrumentCode.value());
            throw new InstrumentSourcePlanNotFoundException(instrumentCode);
        }

        log.info("Instrument source plan deleted: instrumentCode={}", instrumentCode.value());
    }
}
