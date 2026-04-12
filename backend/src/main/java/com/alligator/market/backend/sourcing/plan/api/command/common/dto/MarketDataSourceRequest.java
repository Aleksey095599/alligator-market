package com.alligator.market.backend.sourcing.plan.api.command.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO одного источника рыночных данных в составе плана источников инструмента.
 */
public record MarketDataSourceRequest(

        /* Код провайдера рыночных данных. */
        @NotBlank(message = "providerCode must not be blank")
        String providerCode,

        /* Признак того, что источник включён в план. */
        @NotNull(message = "active must not be null")
        Boolean active,

        /* Приоритет источника: меньшее значение означает более высокий приоритет. */
        @NotNull(message = "priority must not be null")
        @PositiveOrZero(message = "priority must be greater than or equal to 0")
        Integer priority
) {
}
