package com.alligator.market.backend.sourceplan.plan.api.command.replace.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.replace.dto.ReplaceSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.List;
import java.util.Objects;

public class ReplaceSourcePlanMapper {
    private final MarketDataSourceRequestMapper marketDataSourceRequestMapper;

    public ReplaceSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        this.marketDataSourceRequestMapper = Objects.requireNonNull(
                marketDataSourceRequestMapper,
                "marketDataSourceRequestMapper must not be null"
        );
    }

    public SourcePlan toPlan(
            String capturerCode,
            String instrumentCode,
            ReplaceSourcePlanRequest request
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        List<SourcePlanEntry> entries = request.sources().stream()
                .map(marketDataSourceRequestMapper::toDomain)
                .toList();

        return new SourcePlan(
                new MarketDataCapturerCode(capturerCode),
                new InstrumentCode(instrumentCode),
                entries
        );
    }
}
