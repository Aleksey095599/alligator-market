package com.alligator.market.backend.provider.catalog.settings.immutable.web.dto;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Основной DTO для настроек провайдера {@link ProviderSettings}.
 */
public record ProviderSettingsDto(
        @NotBlank String providerCode,
        @Min(1) long minUpdateIntervalSeconds
) {}
