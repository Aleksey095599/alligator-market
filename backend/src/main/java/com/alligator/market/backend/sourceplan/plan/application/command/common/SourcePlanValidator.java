package com.alligator.market.backend.sourceplan.plan.application.command.common;

import com.alligator.market.backend.sourceplan.plan.application.exception.CapturerCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.SourceCodesNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.port.CapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.SourceExistencePort;
import com.alligator.market.domain.instrument.registry.stored.StoredInstrumentRegistry;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class SourcePlanValidator {
    private final CapturerExistencePort capturerExistencePort;

    private final StoredInstrumentRegistry storedInstrumentRegistry;

    private final SourceExistencePort sourceExistencePort;

    public SourcePlanValidator(
            CapturerExistencePort capturerExistencePort,
            StoredInstrumentRegistry storedInstrumentRegistry,
            SourceExistencePort sourceExistencePort
    ) {
        this.capturerExistencePort = Objects.requireNonNull(
                capturerExistencePort,
                "capturerExistencePort must not be null"
        );
        this.storedInstrumentRegistry = Objects.requireNonNull(
                storedInstrumentRegistry,
                "storedInstrumentRegistry must not be null"
        );
        this.sourceExistencePort = Objects.requireNonNull(
                sourceExistencePort,
                "sourceExistencePort must not be null"
        );
    }

    public void ensureCapturerExists(SourcePlan plan) {
        if (!capturerExistencePort.existsByCode(plan.capturerCode())) {
            throw new CapturerCodeNotFoundException(plan.capturerCode());
        }
    }

    public void ensureInstrumentExists(SourcePlan plan) {
        if (!storedInstrumentRegistry.contains(plan.instrumentCode())) {
            throw new InstrumentCodeNotFoundException(plan.instrumentCode());
        }
    }

    public void ensureSourcesExist(SourcePlan plan) {
        Set<String> missingSourceCodes = new LinkedHashSet<>();

        for (PrioritizedSourceCode prioritizedSourceCode : plan.prioritizedSourceCodes()) {
            if (!sourceExistencePort.existsByCode(prioritizedSourceCode.sourceCode())) {
                missingSourceCodes.add(prioritizedSourceCode.sourceCode().value());
            }
        }

        if (!missingSourceCodes.isEmpty()) {
            throw new SourceCodesNotFoundException(missingSourceCodes);
        }
    }
}
