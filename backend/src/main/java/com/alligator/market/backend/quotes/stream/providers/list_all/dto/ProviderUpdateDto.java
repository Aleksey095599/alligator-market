package com.alligator.market.backend.quotes.stream.providers.list_all.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO для обновления провайдера.
 */
public record ProviderUpdateDto(

        @NotBlank
        @Size(max = 255)
        String baseUrl,

        @NotBlank
        @Pattern(regexp = "^(PULL|PUSH)$")
        String mode,

        @NotBlank
        @Size(max = 255)
        String apiKey

) {
}
