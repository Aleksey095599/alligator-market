package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для обновления настроек.
 */
public record FeedSettingsUpdateDto(

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
