package com.alligator.market.backend.sourceplan.plan.api.command.replace.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.replace.dto.ReplaceSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.common.SourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.List;
import java.util.Objects;

public class ReplaceSourcePlanMapper {
    private final SourceRequestMapper sourceRequestMapper;

    public ReplaceSourcePlanMapper(
            SourceRequestMapper sourceRequestMapper
    ) {
        this.sourceRequestMapper = Objects.requireNonNull(
                sourceRequestMapper,
                "sourceRequestMapper must not be null"
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
                .map(sourceRequestMapper::toDomain)
                .toList();

        return new SourcePlan(
                new CapturerCode(capturerCode),
                new InstrumentCode(instrumentCode),
                entries
        );
    }
}
