package com.alligator.market.backend.sourcing.plan.api.command.create.mapper;

import com.alligator.market.backend.sourcing.plan.api.command.create.dto.CreateInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.common.mapper.MarketDataSourceRequestMapper;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Маппер для {@link CreateInstrumentSourcePlanRequest}.
 */
@Component
public class CreateInstrumentSourcePlanMapper {

    private final MarketDataSourceRequestMapper marketDataSourceRequestMapper;

    public CreateInstrumentSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        this.marketDataSourceRequestMapper = Objects.requireNonNull(
                marketDataSourceRequestMapper,
                "marketDataSourceRequestMapper must not be null"
        );
    }

    /**
     * Преобразует {@link CreateInstrumentSourcePlanRequest} в доменную модель {@link InstrumentSourcePlan}.
     */
    public InstrumentSourcePlan toDomain(CreateInstrumentSourcePlanRequest request) {
        List<MarketDataSource> sources = request.sources().stream()
                .map(marketDataSourceRequestMapper::toDomain)
                .toList();

        return new InstrumentSourcePlan(
                new InstrumentCode(request.instrumentCode()),
                sources
        );
    }
}
