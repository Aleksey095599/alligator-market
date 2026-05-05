package com.alligator.market.backend.sourcing.plan.api.command.create.mapper;

import com.alligator.market.backend.sourcing.plan.api.command.create.dto.CreateMarketDataSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.List;
import java.util.Objects;

/**
 * Маппер для {@link CreateMarketDataSourcePlanRequest}.
 */
public class CreateMarketDataSourcePlanMapper {

    private final MarketDataSourceRequestMapper marketDataSourceRequestMapper;

    public CreateMarketDataSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        this.marketDataSourceRequestMapper = Objects.requireNonNull(
                marketDataSourceRequestMapper,
                "marketDataSourceRequestMapper must not be null"
        );
    }

    /**
     * Преобразует запрос {@link CreateMarketDataSourcePlanRequest} в доменную модель {@link MarketDataSourcePlan}.
     */
    public MarketDataSourcePlan toDomain(CreateMarketDataSourcePlanRequest request) {
        List<MarketDataSource> sources = request.sources().stream()
                .map(marketDataSourceRequestMapper::toDomain)
                .toList();

        return new MarketDataSourcePlan(
                new MDCaptureProcessCode(request.captureProcessCode()),
                new InstrumentCode(request.instrumentCode()),
                sources
        );
    }
}
