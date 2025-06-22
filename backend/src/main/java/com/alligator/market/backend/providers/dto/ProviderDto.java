package com.alligator.market.backend.providers.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для представления провайдера.
 */
public record ProviderDto(

        @NotBlank
        @Size(max = 50)
        String name,

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
