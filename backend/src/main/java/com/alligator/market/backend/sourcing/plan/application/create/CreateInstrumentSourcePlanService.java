package com.alligator.market.backend.sourcing.plan.application.create;

import com.alligator.market.backend.sourcing.plan.application.create.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.create.port.ProviderCodeExistencePort;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.InstrumentMarketDataSource;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис создания плана источников рыночных данных для инструмента.
 *
 * <p>Назначение сервиса — выполнить внешние логические проверки перед созданием плана,
 * а затем делегировать сохранение в доменный репозиторий.</p>
 */
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

        /* Проверяем, что инструмент реально существует.
           Это нужно, чтобы дать понятную прикладную ошибку заранее, а не падать позже на FK-ошибке базы данных. */
        ensureInstrumentExists(plan);

        /* Проверяем, что все коды провайдеров сейчас существуют.
           Проверка мягкая: код может устареть позже, но нет смысла позволять пользователю изначально создавать план
           с уже несуществующими провайдерами. */
        ensureProvidersExist(plan);

        // Проверяем, что сценарий действительно является create, а не попыткой тихо заменить уже существующий план.
        ensurePlanDoesNotExist(plan);

        // После прикладных проверок делегируем создание в репозиторий.
        instrumentSourcePlanRepository.create(plan);
    }

    /**
     * Проверяет существование инструмента.
     */
    private void ensureInstrumentExists(InstrumentSourcePlan plan) {
        if (!instrumentCodeExistencePort.existsByCode(plan.instrumentCode())) {
            throw new IllegalArgumentException(
                    "Instrument code '" + plan.instrumentCode().value() + "' does not exist"
            );
        }
    }

    /**
     * Проверяет существование всех провайдеров, указанных в плане.
     */
    private void ensureProvidersExist(InstrumentSourcePlan plan) {
        Set<String> missingProviderCodes = new LinkedHashSet<>();

        for (InstrumentMarketDataSource source : plan.sources()) {
            if (!providerCodeExistencePort.existsByCode(source.providerCode())) {
                missingProviderCodes.add(source.providerCode().value());
            }
        }

        if (!missingProviderCodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Provider codes do not exist: " + String.join(", ", missingProviderCodes)
            );
        }
    }

    /**
     * Проверяет, что план для инструмента ещё не создан.
     */
    private void ensurePlanDoesNotExist(InstrumentSourcePlan plan) {
        if (instrumentSourcePlanRepository.findByInstrumentCode(plan.instrumentCode()).isPresent()) {
            throw new IllegalStateException(
                    "Instrument source plan for instrument '" + plan.instrumentCode().value() + "' already exists"
            );
        }
    }
}
