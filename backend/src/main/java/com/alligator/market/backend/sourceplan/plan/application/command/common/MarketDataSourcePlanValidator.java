package com.alligator.market.backend.sourceplan.plan.application.command.common;

import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataCaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourceCodesNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.InstrumentExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
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
    private final MarketDataSourceExistencePort sourceExistencePort;

    public MarketDataSourcePlanValidator(
            MarketDataCaptureProcessExistencePort captureProcessExistencePort,
            InstrumentExistencePort instrumentExistencePort,
            MarketDataSourceExistencePort sourceExistencePort
    ) {
        this.captureProcessExistencePort = Objects.requireNonNull(
                captureProcessExistencePort,
                "captureProcessExistencePort must not be null"
        );
        this.instrumentExistencePort = Objects.requireNonNull(
                instrumentExistencePort,
                "instrumentExistencePort must not be null"
        );
        this.sourceExistencePort = Objects.requireNonNull(
                sourceExistencePort,
                "sourceExistencePort must not be null"
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
    public void ensureSourcesExist(MarketDataSourcePlan plan) {
        Set<String> missingSourceCodes = new LinkedHashSet<>();

        for (MarketDataSourcePlanEntry entry : plan.entries()) {
            if (!sourceExistencePort.existsByCode(entry.sourceCode())) {
                missingSourceCodes.add(entry.sourceCode().value());
            }
        }

        if (!missingSourceCodes.isEmpty()) {
            throw new MarketDataSourceCodesNotFoundException(missingSourceCodes);
        }
    }
}
