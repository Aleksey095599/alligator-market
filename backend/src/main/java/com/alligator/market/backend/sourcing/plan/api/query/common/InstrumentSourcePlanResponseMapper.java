package com.alligator.market.backend.sourcing.plan.api.query.common;

import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер доменной модели {@link InstrumentSourcePlan} в DTO read-side ответов.
 */
@Component
public class InstrumentSourcePlanResponseMapper {

    /**
     * Конвертирует доменный {@link InstrumentSourcePlan} в DTO ответа {@link InstrumentSourcePlanResponse}.
     */
    public InstrumentSourcePlanResponse toPlanResponse(InstrumentSourcePlan plan) {
        List<MarketDataSourceResponse> sources = plan.sources().stream()
                .map(this::toSourceResponse)
                .toList();

        return new InstrumentSourcePlanResponse(plan.instrumentCode().value(), sources);
    }

    /* Конвертация доменного источника в DTO источника. */
    private MarketDataSourceResponse toSourceResponse(MarketDataSource source) {
        return new MarketDataSourceResponse(
                source.providerCode().value(),
                source.active(),
                source.priority()
        );
    }
}
