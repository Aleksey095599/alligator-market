package com.alligator.market.backend.quotes.stream.settings.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для создания настроек.
 */
public record SettingsCreateDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}$")
        String pair,

        @NotBlank
        @Size(max = 50)
        String provider,

        @NotBlank
        @Pattern(regexp = "^(PULL|PUSH)$")
        String mode,

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
