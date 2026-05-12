package com.alligator.market.backend.sourceplan.plan.api.command.replace.dto;

import com.alligator.market.backend.sourceplan.plan.api.command.common.SourceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReplaceSourcePlanRequest(

        @NotEmpty(message = "sources must not be empty")
        List<@Valid SourceRequest> sources
) {
}
