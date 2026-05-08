package com.alligator.market.backend.sourceplan.plan.api.command.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MarketDataSourceRequest(

        @NotBlank(message = "sourceCode must not be blank")
        String sourceCode,

        @NotNull(message = "priority must not be null")
        @PositiveOrZero(message = "priority must be greater than or equal to 0")
        Integer priority
) {
}
