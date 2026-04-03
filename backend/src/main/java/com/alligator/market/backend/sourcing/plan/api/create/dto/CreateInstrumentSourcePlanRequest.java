package com.alligator.market.backend.sourcing.plan.api.create.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

/**
 * HTTP-запрос на создание плана источников инструмента.
 */
public record CreateInstrumentSourcePlanRequest(

        /* Код инструмента, для которого создаётся план. */
        @NotBlank(message = "instrumentCode must not be blank")
        String instrumentCode,

        /* Источники рыночных данных для инструмента. */
        @NotEmpty(message = "sources must not be empty")
        List<@Valid MarketDataSourceRequest> sources
) {

    /**
     * HTTP-модель одного источника в составе плана.
     */
    public record MarketDataSourceRequest(

            /* Код провайдера-источника. */
            @NotBlank(message = "providerCode must not be blank")
            String providerCode,

            /* Признак активности источника. */
            @NotNull(message = "active must not be null")
            Boolean active,

            /* Приоритет источника. */
            @NotNull(message = "priority must not be null")
            @PositiveOrZero(message = "priority must be greater than or equal to 0")
            Integer priority
    ) {
    }
}
