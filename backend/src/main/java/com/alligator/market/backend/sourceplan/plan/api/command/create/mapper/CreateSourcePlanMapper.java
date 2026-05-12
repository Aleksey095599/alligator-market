package com.alligator.market.backend.sourceplan.plan.api.command.create.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.create.dto.CreateSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.common.SourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.List;
import java.util.Objects;

public class CreateSourcePlanMapper {
    private final SourceRequestMapper sourceRequestMapper;

    public CreateSourcePlanMapper(
            SourceRequestMapper sourceRequestMapper
    ) {
        this.sourceRequestMapper = Objects.requireNonNull(
                sourceRequestMapper,
                "sourceRequestMapper must not be null"
        );
    }

    public SourcePlan toDomain(CreateSourcePlanRequest request) {
        List<SourcePlanEntry> entries = request.sources().stream()
                .map(sourceRequestMapper::toDomain)
                .toList();

        return new SourcePlan(
                new MarketDataCapturerCode(request.capturerCode()),
                new InstrumentCode(request.instrumentCode()),
                entries
        );
    }
}
