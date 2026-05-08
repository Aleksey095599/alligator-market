package com.alligator.market.backend.sourceplan.plan.application.command.replace;

import com.alligator.market.backend.sourceplan.plan.application.command.common.SourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanNotFoundException;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class ReplaceSourcePlanService {
    private final SourcePlanRepository sourcePlanRepository;

    private final SourcePlanValidator existenceValidator;

    public ReplaceSourcePlanService(
            SourcePlanRepository sourcePlanRepository,
            SourcePlanValidator existenceValidator
    ) {
        this.sourcePlanRepository = Objects.requireNonNull(
                sourcePlanRepository,
                "sourcePlanRepository must not be null"
        );
        this.existenceValidator = Objects.requireNonNull(
                existenceValidator,
                "existenceValidator must not be null"
        );
    }

    public void replace(SourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        existenceValidator.ensureMarketDataCapturerExists(plan);

        existenceValidator.ensureInstrumentExists(plan);

        existenceValidator.ensureSourcesExist(plan);

        SourcePlan currentPlan = sourcePlanRepository
                .findByMarketDataCapturerCodeAndInstrumentCode(plan.capturerCode(), plan.instrumentCode())
                .orElseThrow(() -> new SourcePlanNotFoundException(
                        plan.capturerCode(),
                        plan.instrumentCode()
                ));

        if (hasSameSources(currentPlan, plan)) {
            log.info(
                    "Source plan replace skipped: no changes detected, capturerCode={}, instrumentCode={}",
                    plan.capturerCode().value(),
                    plan.instrumentCode().value()
            );
            return;
        }

        boolean replaced = sourcePlanRepository.replaceIfExists(plan);
        if (!replaced) {
            log.warn(
                    "Source plan was not found and was not replaced: capturerCode={}, instrumentCode={}",
                    plan.capturerCode().value(),
                    plan.instrumentCode().value()
            );
            throw new SourcePlanNotFoundException(plan.capturerCode(), plan.instrumentCode());
        }

        log.info(
                "Source plan replaced: capturerCode={}, instrumentCode={}, sourceCount={}",
                plan.capturerCode().value(),
                plan.instrumentCode().value(),
                plan.entries().size()
        );
    }

    private static boolean hasSameSources(SourcePlan currentPlan, SourcePlan newPlan) {
        return normalizeEntries(currentPlan).equals(normalizeEntries(newPlan));
    }

    private static List<SourcePlanEntry> normalizeEntries(SourcePlan plan) {
        return plan.entries().stream()
                .sorted(Comparator
                        .comparingInt(SourcePlanEntry::priority)
                        .thenComparing(entry -> entry.sourceCode().value()))
                .toList();
    }
}
