package com.alligator.market.backend.sourcing.plan.api.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Источник рыночных данных для HTTP-запросов.
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
