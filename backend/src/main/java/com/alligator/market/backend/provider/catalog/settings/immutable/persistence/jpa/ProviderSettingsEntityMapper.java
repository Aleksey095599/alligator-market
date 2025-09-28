package com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Маппер: сущность ⇄ доменная модель.
 */
@Component
public class ProviderSettingsEntityMapper {

    /** Сущность ⇒ доменная модель. */
    public ProviderSettings toDomain(ProviderSettingsEntity entity) {
        return new ProviderSettings(
                entity.getMinUpdateIntervalSeconds()
        );
    }

    /** Доменная модель ⇒ сущность. */
    public ProviderSettingsEntity toEntity(String providerCode, ProviderSettings settings) {
        var entity = new ProviderSettingsEntity();
        entity.setProviderCode(providerCode);
        entity.setMinUpdateIntervalSeconds(settings.minUpdateIntervalSeconds());
        return entity;
    }
}
