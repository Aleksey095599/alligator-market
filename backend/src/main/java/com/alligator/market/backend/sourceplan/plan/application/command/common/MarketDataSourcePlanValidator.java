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
 * Validates that entities referenced by a source plan exist.
 */
public final class MarketDataSourcePlanValidator {

    /* Capture process existence check. */
    private final MarketDataCaptureProcessExistencePort captureProcessExistencePort;

    /* Instrument existence check. */
    private final InstrumentExistencePort instrumentExistencePort;

    /* Market data source existence check. */
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
     * Ensures that the capture process referenced by the plan exists.
     */
    public void ensureMarketDataCaptureProcessExists(MarketDataSourcePlan plan) {
        if (!captureProcessExistencePort.existsByCode(plan.captureProcessCode())) {
            throw new MarketDataCaptureProcessCodeNotFoundException(plan.captureProcessCode());
        }
    }

    /**
     * Ensures that the instrument referenced by the plan exists.
     */
    public void ensureInstrumentExists(MarketDataSourcePlan plan) {
        if (!instrumentExistencePort.existsByCode(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    /**
     * Ensures that all sources referenced by the plan exist.
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
