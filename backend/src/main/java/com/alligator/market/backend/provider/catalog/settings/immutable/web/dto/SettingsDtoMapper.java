package com.alligator.market.backend.provider.catalog.settings.immutable.web.dto;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Маппер: DTO ⇄ доменная модель.
 */
@Component
public class SettingsDtoMapper {

    /** Доменная модель ⇒ основной DTO. */
    public SettingsDto toDto(ProviderSettings settings) {
        return new SettingsDto(
                settings.providerCode(),
                settings.minUpdateIntervalSeconds().getSeconds()
        );
    }

    /** Основной DTO ⇒ доменная модель. */
    public ProviderSettings toDomain(SettingsDto dto) {
        return new ProviderSettings(
                dto.providerCode(),
                Duration.ofSeconds(dto.minUpdateIntervalSeconds())
        );
    }
}
