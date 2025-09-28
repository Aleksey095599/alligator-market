package com.alligator.market.backend.provider.catalog.settings.immutable.web.dto;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Маппер: DTO ⇄ доменная модель.
 */
@Component
public class ProviderSettingsDtoMapper {

    /** Доменная модель ⇒ основной DTO. */
    public ProviderSettingsDto toDto(ProviderSettings settings) {
        return new ProviderSettingsDto(
                settings.providerCode(),
                settings.minUpdateIntervalSeconds().getSeconds()
        );
    }

    /** Основной DTO ⇒ доменная модель. */
    public ProviderSettings toDomain(ProviderSettingsDto dto) {
        return new ProviderSettings(
                dto.providerCode(),
                Duration.ofSeconds(dto.minUpdateIntervalSeconds())
        );
    }
}
