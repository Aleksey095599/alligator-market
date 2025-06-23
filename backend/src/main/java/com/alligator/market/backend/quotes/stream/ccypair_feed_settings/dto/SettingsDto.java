package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для представления настроек.
 */
public record SettingsDto(

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
        Integer refreshMs, // для PUSH игнорируется

        @NotNull
        Boolean enabled

) {
}
