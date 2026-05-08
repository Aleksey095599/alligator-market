package com.alligator.market.backend.sourceplan.plan.api.command.create.dto;

import com.alligator.market.backend.sourceplan.plan.api.command.common.MarketDataSourceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateSourcePlanRequest(

        @NotBlank(message = "capturerCode must not be blank")
        String capturerCode,

        @NotBlank(message = "instrumentCode must not be blank")
        String instrumentCode,

        @NotEmpty(message = "sources must not be empty")
        List<@Valid MarketDataSourceRequest> sources
) {
}
