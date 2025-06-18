package com.alligator.market.backend.quotes.config.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для создания конфигурации стрима котировок.
 */
public record FxPairStreamingConfigCreateDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}$")
        String pair,

        @NotBlank
        @Size(max = 50)
        String provider,

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
