package com.alligator.market.backend.sourceplan.plan.api.command.create.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.create.dto.CreateSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.common.SourceRequestMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import com.alligator.market.domain.sourceplan.SourcePlan;

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
        List<PrioritizedSourceCode> prioritizedSourceCodes = request.sources().stream()
                .map(sourceRequestMapper::toDomain)
                .toList();

        return new SourcePlan(
                new SourcePlanKey(
                        new CapturerCode(request.capturerCode()),
                        new InstrumentCode(request.instrumentCode())
                ),
                prioritizedSourceCodes
        );
    }
}
