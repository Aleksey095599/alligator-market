package com.alligator.market.backend.fx.pairs.dto;

import jakarta.validation.constraints.*;

/* DTO для обновления валютной пары. */
public record PairUpdateDto(

        /* Кол-во знаков после запятой для курса */
        @NotNull
        @Min(0) @Max(10)
        Integer decimal
) {
}
