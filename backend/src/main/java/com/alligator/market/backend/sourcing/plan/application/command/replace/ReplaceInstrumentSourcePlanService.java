package com.alligator.market.backend.sourcing.plan.application.command.replace;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис полной замены плана источников рыночных данных для инструмента.
 */
@Slf4j
public final class ReplaceInstrumentSourcePlanService {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    /* Порт проверки существования инструмента по коду. */
    private final InstrumentCodeExistencePort instrumentCodeExistencePort;

    /* Порт проверки существования провайдера по коду. */
    private final ProviderCodeExistencePort providerCodeExistencePort;

    public ReplaceInstrumentSourcePlanService(
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
     * Полностью заменяет содержимое существующего плана источников.
     */
    public void replace(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        // Проверяем, что инструмент реально существует
        ensureInstrumentExists(plan);

        // Проверяем, что все коды провайдеров из плана существуют
        ensureProvidersExist(plan);

        // Условно заменяем содержимое root-plan и сигнализируем, если плана не было
        boolean replaced = instrumentSourcePlanRepository.replaceIfExists(plan);
        if (!replaced) {
            log.warn("Instrument source plan was not found and was not replaced: instrumentCode={}", plan.instrumentCode().value());
            throw new InstrumentSourcePlanNotFoundException(plan.instrumentCode());
        }

        log.info("Instrument source plan replaced: instrumentCode={}, sourceCount={}", plan.instrumentCode().value(), plan.sources().size());
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
