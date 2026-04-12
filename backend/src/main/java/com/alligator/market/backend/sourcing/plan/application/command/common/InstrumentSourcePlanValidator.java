package com.alligator.market.backend.sourcing.plan.application.command.common;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Валидатор существования связанных сущностей для плана источников.
 */
public final class InstrumentSourcePlanValidator {

    /* Порт проверки существования инструмента по коду. */
    private final InstrumentCodeExistencePort instrumentCodeExistencePort;

    /* Порт проверки существования провайдера по коду. */
    private final ProviderCodeExistencePort providerCodeExistencePort;

    public InstrumentSourcePlanValidator(
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
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
     * Проверяет существование инструмента.
     */
    public void ensureInstrumentExists(InstrumentSourcePlan plan) {
        if (!instrumentCodeExistencePort.existsByCode(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    /**
     * Проверяет существование всех провайдеров, указанных в плане.
     */
    public void ensureProvidersExist(InstrumentSourcePlan plan) {
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
