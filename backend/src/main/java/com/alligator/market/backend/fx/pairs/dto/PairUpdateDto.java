package com.alligator.market.backend.fx.pairs.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/* DTO для обновления валютной пары. */
public record PairUpdateDto(

        @NotNull
        @Min(0) @Max(10)
        Integer decimal
) {
}
