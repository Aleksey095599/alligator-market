package com.alligator.market.backend.sourcing.plan.api.command.create.mapper;

import com.alligator.market.backend.sourcing.plan.api.command.create.dto.CreateInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.common.dto.MarketDataSourceRequest;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.List;

/**
 * Маппер создания плана источников инструмента.
 */
public class CreateInstrumentSourcePlanMapper {

    /**
     * Преобразует тело HTTP-запроса в доменную модель плана.
     */
    public InstrumentSourcePlan toPlan(CreateInstrumentSourcePlanRequest request) {
        List<MarketDataSource> sources = request.sources().stream()
                .map(this::toSource)
                .toList();

        return new InstrumentSourcePlan(
                new InstrumentCode(request.instrumentCode()),
                sources
        );
    }
}
