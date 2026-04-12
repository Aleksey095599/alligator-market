package com.alligator.market.backend.sourcing.plan.application.command.replace;

import com.alligator.market.backend.sourcing.plan.application.command.common.InstrumentSourcePlanExistenceValidator;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис полной замены плана источников рыночных данных для инструмента.
 */
@Slf4j
public final class ReplaceInstrumentSourcePlanService {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    /* Валидатор существования инструмента и провайдеров из плана. */
    private final InstrumentSourcePlanExistenceValidator existenceValidator;

    public ReplaceInstrumentSourcePlanService(
            InstrumentSourcePlanRepository instrumentSourcePlanRepository,
            InstrumentSourcePlanExistenceValidator existenceValidator
    ) {
        this.instrumentSourcePlanRepository = Objects.requireNonNull(
                instrumentSourcePlanRepository,
                "instrumentSourcePlanRepository must not be null"
        );
        this.existenceValidator = Objects.requireNonNull(
                existenceValidator,
                "existenceValidator must not be null"
        );
    }

    /**
     * Полностью заменяет содержимое существующего плана источников.
     */
    public void replace(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        // Проверяем, что инструмент реально существует
        existenceValidator.ensureInstrumentExists(plan);

        // Проверяем, что все коды провайдеров из плана существуют
        existenceValidator.ensureProvidersExist(plan);

        // Условно заменяем содержимое root-plan и сигнализируем, если плана не было
        boolean replaced = instrumentSourcePlanRepository.replaceIfExists(plan);
        if (!replaced) {
            log.warn("Instrument source plan was not found and was not replaced: instrumentCode={}", plan.instrumentCode().value());
            throw new InstrumentSourcePlanNotFoundException(plan.instrumentCode());
        }

        log.info("Instrument source plan replaced: instrumentCode={}, sourceCount={}", plan.instrumentCode().value(), plan.sources().size());
    }
}
