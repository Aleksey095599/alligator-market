package com.alligator.market.backend.sourcing.plan.api.command.replace.dto;

import com.alligator.market.backend.sourcing.plan.api.command.common.MarketDataSourceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Тело HTTP-запроса на полную замену состава источников в плане инструмента.
 */
public record ReplaceInstrumentSourcePlanRequest(

        /* Новый полный состав источников, который должен заменить текущий план. */
        @NotEmpty(message = "sources must not be empty")
        List<@Valid MarketDataSourceRequest> sources
) {
}
