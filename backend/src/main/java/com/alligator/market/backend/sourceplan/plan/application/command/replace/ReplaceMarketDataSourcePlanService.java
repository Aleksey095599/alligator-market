package com.alligator.market.backend.sourceplan.plan.application.command.replace;

import com.alligator.market.backend.sourceplan.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Service for replacing market data source plans.
 */
@Slf4j
public final class ReplaceMarketDataSourcePlanService {

    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    /* Validates entities referenced by the plan. */
    private final MarketDataSourcePlanValidator existenceValidator;

    public ReplaceMarketDataSourcePlanService(
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            MarketDataSourcePlanValidator existenceValidator
    ) {
        this.marketDataSourcePlanRepository = Objects.requireNonNull(
                marketDataSourcePlanRepository,
                "marketDataSourcePlanRepository must not be null"
        );
        this.existenceValidator = Objects.requireNonNull(
                existenceValidator,
                "existenceValidator must not be null"
        );
    }

    /**
     * Replaces the contents of an existing source plan.
     */
    public void replace(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        existenceValidator.ensureMarketDataCapturerExists(plan);

        existenceValidator.ensureInstrumentExists(plan);

        existenceValidator.ensureSourcesExist(plan);

        MarketDataSourcePlan currentPlan = marketDataSourcePlanRepository
                .findByMarketDataCapturerCodeAndInstrumentCode(plan.capturerCode(), plan.instrumentCode())
                .orElseThrow(() -> new MarketDataSourcePlanNotFoundException(
                        plan.capturerCode(),
                        plan.instrumentCode()
                ));

        if (hasSameSources(currentPlan, plan)) {
            log.info(
                    "Market data source plan replace skipped: no changes detected, capturerCode={}, instrumentCode={}",
                    plan.capturerCode().value(),
                    plan.instrumentCode().value()
            );
            return;
        }

        boolean replaced = marketDataSourcePlanRepository.replaceIfExists(plan);
        if (!replaced) {
            log.warn(
                    "Market data source plan was not found and was not replaced: capturerCode={}, instrumentCode={}",
                    plan.capturerCode().value(),
                    plan.instrumentCode().value()
            );
            throw new MarketDataSourcePlanNotFoundException(plan.capturerCode(), plan.instrumentCode());
        }

        log.info(
                "Market data source plan replaced: capturerCode={}, instrumentCode={}, sourceCount={}",
                plan.capturerCode().value(),
                plan.instrumentCode().value(),
                plan.entries().size()
        );
    }

    private static boolean hasSameSources(MarketDataSourcePlan currentPlan, MarketDataSourcePlan newPlan) {
        return normalizeEntries(currentPlan).equals(normalizeEntries(newPlan));
    }

    private static List<MarketDataSourcePlanEntry> normalizeEntries(MarketDataSourcePlan plan) {
        return plan.entries().stream()
                .sorted(Comparator
                        .comparingInt(MarketDataSourcePlanEntry::priority)
                        .thenComparing(entry -> entry.sourceCode().value()))
                .toList();
    }
}
