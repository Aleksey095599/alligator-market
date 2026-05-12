package com.alligator.market.backend.sourceplan.plan.application.command.common;

import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataCapturerCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourceCodesNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
import com.alligator.market.domain.instrument.registry.InstrumentRegistry;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class SourcePlanValidator {
    private final MarketDataCapturerExistencePort capturerExistencePort;

    private final InstrumentRegistry instrumentRegistry;

    private final MarketDataSourceExistencePort sourceExistencePort;

    public SourcePlanValidator(
            MarketDataCapturerExistencePort capturerExistencePort,
            InstrumentRegistry instrumentRegistry,
            MarketDataSourceExistencePort sourceExistencePort
    ) {
        this.capturerExistencePort = Objects.requireNonNull(
                capturerExistencePort,
                "capturerExistencePort must not be null"
        );
        this.instrumentRegistry = Objects.requireNonNull(
                instrumentRegistry,
                "instrumentRegistry must not be null"
        );
        this.sourceExistencePort = Objects.requireNonNull(
                sourceExistencePort,
                "sourceExistencePort must not be null"
        );
    }

    public void ensureMarketDataCapturerExists(SourcePlan plan) {
        if (!capturerExistencePort.existsByCode(plan.capturerCode())) {
            throw new MarketDataCapturerCodeNotFoundException(plan.capturerCode());
        }
    }

    public void ensureInstrumentExists(SourcePlan plan) {
        if (!instrumentRegistry.contains(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    public void ensureSourcesExist(SourcePlan plan) {
        Set<String> missingSourceCodes = new LinkedHashSet<>();

        for (SourcePlanEntry entry : plan.entries()) {
            if (!sourceExistencePort.existsByCode(entry.sourceCode())) {
                missingSourceCodes.add(entry.sourceCode().value());
            }
        }

        if (!missingSourceCodes.isEmpty()) {
            throw new MarketDataSourceCodesNotFoundException(missingSourceCodes);
        }
    }
}
