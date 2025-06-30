package com.alligator.market.backend.quotes.ccypair_feed_settings.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для представления настроек.
 */
public record FeedSettingsDto(

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
        @Min(1000)
        Integer fetchPeriodMs, // для PUSH игнорируется, минимум 1000 мс

        @NotNull
        Boolean enabled

) {
}
