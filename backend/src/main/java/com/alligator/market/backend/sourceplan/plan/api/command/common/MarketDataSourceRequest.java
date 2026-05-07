package com.alligator.market.backend.sourceplan.plan.api.command.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Market data source payload for source plan commands.
 */
public record MarketDataSourceRequest(

        @NotBlank(message = "providerCode must not be blank")
        String providerCode,

        @NotNull(message = "priority must not be null")
        @PositiveOrZero(message = "priority must be greater than or equal to 0")
        Integer priority
) {
}
