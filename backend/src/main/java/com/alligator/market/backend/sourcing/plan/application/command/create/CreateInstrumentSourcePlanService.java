package com.alligator.market.backend.sourcing.plan.application.command.create;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.command.common.InstrumentSourcePlanExistenceValidator;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис создания плана источников рыночных данных для инструмента.
 *
 * <p>Назначение сервиса — выполнить внешние логические проверки перед созданием плана,
 * а затем делегировать сохранение в доменный репозиторий.</p>
 */
@Slf4j
public final class CreateInstrumentSourcePlanService {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    /* Валидатор существования инструмента и провайдеров из плана. */
    private final InstrumentSourcePlanExistenceValidator existenceValidator;

    public CreateInstrumentSourcePlanService(
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
     * Создаёт новый план источников для инструмента.
     */
    public void create(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        // Проверяем, что инструмент реально существует
        existenceValidator.ensureInstrumentExists(plan);

        // Проверяем, что все коды провайдеров из плана существуют
        existenceValidator.ensureProvidersExist(plan);

        // Атомарно создаём план, если он ещё не существует
        if (!instrumentSourcePlanRepository.createIfAbsent(plan)) {
            log.warn("Instrument source plan already exists and was not created: instrumentCode={}", plan.instrumentCode().value());
            throw new InstrumentSourcePlanAlreadyExistsException(plan.instrumentCode());
        }

        log.info("Instrument source plan created: instrumentCode={}, sourceCount={}", plan.instrumentCode().value(), plan.sources().size());
    }
}
