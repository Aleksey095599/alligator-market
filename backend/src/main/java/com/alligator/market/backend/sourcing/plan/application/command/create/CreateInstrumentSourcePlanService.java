package com.alligator.market.backend.sourcing.plan.application.command.create;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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

    /* Порт проверки существования инструмента по коду. */
    private final InstrumentCodeExistencePort instrumentCodeExistencePort;

    /* Порт проверки существования провайдера по коду. */
    private final ProviderCodeExistencePort providerCodeExistencePort;

    public CreateInstrumentSourcePlanService(
            InstrumentSourcePlanRepository instrumentSourcePlanRepository,
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
        this.instrumentSourcePlanRepository = Objects.requireNonNull(
                instrumentSourcePlanRepository,
                "instrumentSourcePlanRepository must not be null"
        );
        this.instrumentCodeExistencePort = Objects.requireNonNull(
                instrumentCodeExistencePort,
                "instrumentCodeExistencePort must not be null"
        );
        this.providerCodeExistencePort = Objects.requireNonNull(
                providerCodeExistencePort,
                "providerCodeExistencePort must not be null"
        );
    }

    /**
     * Создаёт новый план источников для инструмента.
     */
    public void create(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        // Проверяем, что инструмент реально существует
        ensureInstrumentExists(plan);

        // Проверяем, что все коды провайдеров из плана существуют
        ensureProvidersExist(plan);

        // Атомарно создаём план, если он ещё не существует
        if (!instrumentSourcePlanRepository.createIfAbsent(plan)) {
            log.warn("Instrument source plan already exists and was not created: instrumentCode={}", plan.instrumentCode().value());
            throw new InstrumentSourcePlanAlreadyExistsException(plan.instrumentCode());
        }

        log.info("Instrument source plan created: instrumentCode={}, sourceCount={}", plan.instrumentCode().value(), plan.sources().size());
    }

    /**
     * Проверяет существование инструмента.
     */
    private void ensureInstrumentExists(InstrumentSourcePlan plan) {
        if (!instrumentCodeExistencePort.existsByCode(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    /**
     * Проверяет существование всех провайдеров, указанных в плане.
     */
    private void ensureProvidersExist(InstrumentSourcePlan plan) {
        Set<String> missingProviderCodes = new LinkedHashSet<>();

        for (MarketDataSource source : plan.sources()) {
            if (!providerCodeExistencePort.existsByCode(source.providerCode())) {
                missingProviderCodes.add(source.providerCode().value());
            }
        }

        if (!missingProviderCodes.isEmpty()) {
            throw new ProviderCodesNotFoundException(missingProviderCodes);
        }
    }

}
