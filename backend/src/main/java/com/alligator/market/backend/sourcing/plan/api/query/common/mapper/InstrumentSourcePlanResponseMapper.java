package com.alligator.market.backend.sourcing.plan.api.query.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.query.get.dto.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.get.dto.MarketDataSourceResponse;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Единый mapper доменной модели плана в read-side HTTP DTO.
 */
@Component
public class InstrumentSourcePlanResponseMapper {

    /**
     * Конвертирует доменный план в DTO ответа.
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
