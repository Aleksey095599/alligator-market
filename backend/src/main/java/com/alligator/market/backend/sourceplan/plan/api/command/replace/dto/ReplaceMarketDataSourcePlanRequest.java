package com.alligator.market.backend.sourceplan.plan.api.command.replace.dto;

import com.alligator.market.backend.sourceplan.plan.api.command.common.MarketDataSourceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Request DTO use case полная замена состава источников в плане инструмента.
 */
public record ReplaceMarketDataSourcePlanRequest(

        /* Новый полный состав источников, который должен заменить текущий план. */
        @NotEmpty(message = "sources must not be empty")
        List<@Valid MarketDataSourceRequest> sources
) {
}
