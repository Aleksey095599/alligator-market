package com.alligator.market.backend.sourcing.plan.application.command.common;

import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataCaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderExistencePort;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Валидатор существования связанных сущностей для плана источников.
 */
public final class MarketDataSourcePlanValidator {

    /* Порт проверки существования процесса захвата по коду. */
    private final MarketDataCaptureProcessExistencePort captureProcessExistencePort;

    /* Порт проверки существования инструмента по коду. */
    private final InstrumentExistencePort instrumentExistencePort;

    /* Порт проверки существования провайдера по коду. */
    private final ProviderExistencePort providerExistencePort;

    public MarketDataSourcePlanValidator(
            MarketDataCaptureProcessExistencePort captureProcessExistencePort,
            InstrumentExistencePort instrumentExistencePort,
            ProviderExistencePort providerExistencePort
    ) {
        this.captureProcessExistencePort = Objects.requireNonNull(
                captureProcessExistencePort,
                "captureProcessExistencePort must not be null"
        );
        this.instrumentExistencePort = Objects.requireNonNull(
                instrumentExistencePort,
                "instrumentExistencePort must not be null"
        );
        this.providerExistencePort = Objects.requireNonNull(
                providerExistencePort,
                "providerExistencePort must not be null"
        );
    }

    /**
     * Проверяет существование процесса захвата.
     */
    public void ensureMarketDataCaptureProcessExists(MarketDataSourcePlan plan) {
        if (!captureProcessExistencePort.existsByCode(plan.captureProcessCode())) {
            throw new MarketDataCaptureProcessCodeNotFoundException(plan.captureProcessCode());
        }
    }

    /**
     * Проверяет существование инструмента.
     */
    public void ensureInstrumentExists(MarketDataSourcePlan plan) {
        if (!instrumentExistencePort.existsByCode(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    /**
     * Проверяет существование всех провайдеров, указанных в плане.
     */
    public void ensureProvidersExist(MarketDataSourcePlan plan) {
        Set<String> missingProviderCodes = new LinkedHashSet<>();

        for (MarketDataSourcePlanEntry entry : plan.entries()) {
            if (!providerExistencePort.existsByCode(entry.providerCode())) {
                missingProviderCodes.add(entry.providerCode().value());
            }
        }

        if (!missingProviderCodes.isEmpty()) {
            throw new ProviderCodesNotFoundException(missingProviderCodes);
        }
    }
}
