package com.alligator.market.backend.providers.dto;

import jakarta.validation.constraints.*;

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
