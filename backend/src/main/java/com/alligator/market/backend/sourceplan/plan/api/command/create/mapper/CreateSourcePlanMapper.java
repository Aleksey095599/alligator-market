package com.alligator.market.backend.sourceplan.plan.api.command.create.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.create.dto.CreateSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.List;
import java.util.Objects;

/**
 * Маппер для {@link CreateSourcePlanRequest}.
 */
public class CreateSourcePlanMapper {

    private final MarketDataSourceRequestMapper marketDataSourceRequestMapper;

    public CreateSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        this.marketDataSourceRequestMapper = Objects.requireNonNull(
                marketDataSourceRequestMapper,
                "marketDataSourceRequestMapper must not be null"
        );
    }

    /**
     * Преобразует запрос {@link CreateSourcePlanRequest} в доменную модель {@link SourcePlan}.
     */
    public SourcePlan toDomain(CreateSourcePlanRequest request) {
        List<SourcePlanEntry> entries = request.sources().stream()
                .map(marketDataSourceRequestMapper::toDomain)
                .toList();

        return new SourcePlan(
                new MarketDataCapturerCode(request.capturerCode()),
                new InstrumentCode(request.instrumentCode()),
                entries
        );
    }
}
