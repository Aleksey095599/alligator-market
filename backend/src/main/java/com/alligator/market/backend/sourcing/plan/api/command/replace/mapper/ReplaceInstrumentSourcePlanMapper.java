package com.alligator.market.backend.sourcing.plan.api.command.replace.mapper;

import com.alligator.market.backend.sourcing.plan.api.command.replace.dto.ReplaceInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.List;
import java.util.Objects;

/**
 * Маппер для {@link ReplaceInstrumentSourcePlanRequest}.
 */
public class ReplaceInstrumentSourcePlanMapper {

    private final MarketDataSourceRequestMapper marketDataSourceRequestMapper;

    public ReplaceInstrumentSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        this.marketDataSourceRequestMapper = Objects.requireNonNull(
                marketDataSourceRequestMapper,
                "marketDataSourceRequestMapper must not be null"
        );
    }

    /**
     * Преобразует запрос {@link ReplaceInstrumentSourcePlanRequest} в доменную модель {@link InstrumentSourcePlan}.
     */
    public InstrumentSourcePlan toPlan(String instrumentCode, ReplaceInstrumentSourcePlanRequest request) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        List<MarketDataSource> sources = request.sources().stream()
                .map(marketDataSourceRequestMapper::toDomain)
                .toList();

        return new InstrumentSourcePlan(
                new InstrumentCode(instrumentCode),
                sources
        );
    }
}
