package com.alligator.market.backend.sourcing.plan.application.command.common;

import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataCaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Валидатор существования связанных сущностей для плана источников.
 */
public final class MarketDataSourcePlanValidator {

    /* Порт проверки существования процесса захвата по коду. */
    private final MarketDataCaptureProcessCodeExistencePort captureProcessCodeExistencePort;

    /* Порт проверки существования инструмента по коду. */
    private final InstrumentCodeExistencePort instrumentCodeExistencePort;

    /* Порт проверки существования провайдера по коду. */
    private final ProviderCodeExistencePort providerCodeExistencePort;

    public MarketDataSourcePlanValidator(
            MarketDataCaptureProcessCodeExistencePort captureProcessCodeExistencePort,
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
        this.captureProcessCodeExistencePort = Objects.requireNonNull(
                captureProcessCodeExistencePort,
                "captureProcessCodeExistencePort must not be null"
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
     * Проверяет существование процесса захвата.
     */
    public void ensureMarketDataCaptureProcessExists(MarketDataSourcePlan plan) {
        if (!captureProcessCodeExistencePort.existsByCode(plan.captureProcessCode())) {
            throw new MarketDataCaptureProcessCodeNotFoundException(plan.captureProcessCode());
        }
    }

    /**
     * Проверяет существование инструмента.
     */
    public void ensureInstrumentExists(MarketDataSourcePlan plan) {
        if (!instrumentCodeExistencePort.existsByCode(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    /**
     * Проверяет существование всех провайдеров, указанных в плане.
     */
    public void ensureProvidersExist(MarketDataSourcePlan plan) {
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
