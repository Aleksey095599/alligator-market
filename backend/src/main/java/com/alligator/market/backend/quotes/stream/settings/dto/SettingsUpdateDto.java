package com.alligator.market.backend.quotes.stream.settings.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для обновления настроек.
 */
public record SettingsUpdateDto(

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
