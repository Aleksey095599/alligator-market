package com.alligator.market.backend.sourceplan.plan.application.command.delete;

import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class DeleteSourcePlanService {
    private final SourcePlanRepository sourcePlanRepository;
    private final RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater;

    public DeleteSourcePlanService(
            SourcePlanRepository sourcePlanRepository,
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater
    ) {
        this.sourcePlanRepository = Objects.requireNonNull(
                sourcePlanRepository,
                "sourcePlanRepository must not be null"
        );
        this.runtimeSourcePlanRegistryUpdater = Objects.requireNonNull(
                runtimeSourcePlanRegistryUpdater,
                "runtimeSourcePlanRegistryUpdater must not be null"
        );
    }

    public void delete(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        boolean deleted = sourcePlanRepository
                .deleteIfExistsByKey(new SourcePlanKey(capturerCode, instrumentCode));
        if (!deleted) {
            log.warn(
                    "Source plan was not found and was not deleted: capturerCode={}, instrumentCode={}",
                    capturerCode.value(),
                    instrumentCode.value()
            );
            throw new SourcePlanNotFoundException(capturerCode, instrumentCode);
        }

        runtimeSourcePlanRegistryUpdater.updateRuntimeRegistry();

        log.info(
                "Source plan deleted: capturerCode={}, instrumentCode={}",
                capturerCode.value(),
                instrumentCode.value()
        );
    }
}
