package com.alligator.market.backend.sourceplan.plan.application.command.create;

import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourceplan.plan.application.command.common.SourcePlanValidator;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class CreateSourcePlanService {
    private final SourcePlanRepository sourcePlanRepository;

    private final SourcePlanValidator existenceValidator;

    private final RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater;

    public CreateSourcePlanService(
            SourcePlanRepository sourcePlanRepository,
            SourcePlanValidator existenceValidator,
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater
    ) {
        this.sourcePlanRepository = Objects.requireNonNull(
                sourcePlanRepository,
                "sourcePlanRepository must not be null"
        );
        this.existenceValidator = Objects.requireNonNull(
                existenceValidator,
                "existenceValidator must not be null"
        );
        this.runtimeSourcePlanRegistryUpdater = Objects.requireNonNull(
                runtimeSourcePlanRegistryUpdater,
                "runtimeSourcePlanRegistryUpdater must not be null"
        );
    }

    public void create(SourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        existenceValidator.ensureCapturerExists(plan);

        existenceValidator.ensureInstrumentExists(plan);

        existenceValidator.ensureSourcesExist(plan);

        if (!sourcePlanRepository.createIfAbsent(plan)) {
            log.warn(
                    "Source plan already exists and was not created: capturerCode={}, instrumentCode={}",
                    plan.capturerCode().value(),
                    plan.instrumentCode().value()
            );
            throw new SourcePlanAlreadyExistsException(plan.capturerCode(), plan.instrumentCode());
        }

        runtimeSourcePlanRegistryUpdater.updateRuntimeRegistry();

        log.info(
                "Source plan created: capturerCode={}, instrumentCode={}, sourceCount={}",
                plan.capturerCode().value(),
                plan.instrumentCode().value(),
                plan.prioritizedSourceCodes().size()
        );
    }
}
