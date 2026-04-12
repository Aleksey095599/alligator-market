package com.alligator.market.backend.sourcing.plan.api.command.create.dto;

import com.alligator.market.backend.sourcing.plan.api.command.common.dto.MarketDataSourceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO тела HTTP-запроса на создание плана источников для инструмента.
 */
public record CreateInstrumentSourcePlanRequest(

        /* Код инструмента, для которого создаётся новый план источников. */
        @NotBlank(message = "instrumentCode must not be blank")
        String instrumentCode,

        /* Список источников, формирующих создаваемый план. */
        @NotEmpty(message = "sources must not be empty")
        List<@Valid MarketDataSourceRequest> sources
) {
}
