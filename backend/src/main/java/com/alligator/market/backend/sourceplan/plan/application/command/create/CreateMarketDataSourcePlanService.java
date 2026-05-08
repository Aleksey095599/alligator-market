package com.alligator.market.backend.sourceplan.plan.application.command.create;

import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourceplan.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Service for creating market data source plans.
 *
 * <p>Runs external existence checks before delegating persistence to the domain repository.</p>
 */
@Slf4j
public final class CreateMarketDataSourcePlanService {

    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    /* Validates entities referenced by the plan. */
    private final MarketDataSourcePlanValidator existenceValidator;

    public CreateMarketDataSourcePlanService(
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
     * Creates a new source plan.
     */
    public void create(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        existenceValidator.ensureMarketDataCapturerExists(plan);

        existenceValidator.ensureInstrumentExists(plan);

        existenceValidator.ensureSourcesExist(plan);

        if (!marketDataSourcePlanRepository.createIfAbsent(plan)) {
            log.warn(
                    "Market data source plan already exists and was not created: capturerCode={}, instrumentCode={}",
                    plan.capturerCode().value(),
                    plan.instrumentCode().value()
            );
            throw new MarketDataSourcePlanAlreadyExistsException(plan.capturerCode(), plan.instrumentCode());
        }

        log.info(
                "Market data source plan created: capturerCode={}, instrumentCode={}, sourceCount={}",
                plan.capturerCode().value(),
                plan.instrumentCode().value(),
                plan.entries().size()
        );
    }
}
