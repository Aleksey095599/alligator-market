package com.alligator.market.backend.quotes.config.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для обновления конфигурации стрима котировок.
 */
public record FxPairStreamingConfigUpdateDto(

        @NotNull
        @Min(0) @Max(32767)
        Short priority,

        @NotNull
        @Min(0)
        Integer refreshMs,

        @NotNull
        Boolean enabled

) {
}
