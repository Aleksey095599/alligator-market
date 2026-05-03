package com.alligator.market.backend.sourcing.plan.api.command.replace.mapper;

import com.alligator.market.backend.sourcing.plan.api.command.replace.dto.ReplaceMarketDataSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.List;
import java.util.Objects;

/**
 * Маппер для {@link ReplaceMarketDataSourcePlanRequest}.
 */
public class ReplaceMarketDataSourcePlanMapper {

    private final MarketDataSourceRequestMapper marketDataSourceRequestMapper;

    public ReplaceMarketDataSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        this.marketDataSourceRequestMapper = Objects.requireNonNull(
                marketDataSourceRequestMapper,
                "marketDataSourceRequestMapper must not be null"
        );
    }

    /**
     * Преобразует запрос {@link ReplaceMarketDataSourcePlanRequest} в доменную модель {@link MarketDataSourcePlan}.
     */
    public MarketDataSourcePlan toPlan(String instrumentCode, ReplaceMarketDataSourcePlanRequest request) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        List<MarketDataSource> sources = request.sources().stream()
                .map(marketDataSourceRequestMapper::toDomain)
                .toList();

        return new MarketDataSourcePlan(
                new InstrumentCode(instrumentCode),
                sources
        );
    }
}
