package com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import org.springframework.stereotype.Component;

/**
 * Маппер: сущность ⇄ доменная модель.
 */
@Component
public class SettingsEntityMapper {

    /** Сущность ⇒ доменная модель. */
    public ProviderSettings toDomain(SettingsEntity entity) {
        return new ProviderSettings(
                entity.getMinUpdateIntervalSeconds()
        );
    }

    /** Доменная модель ⇒ сущность. */
    public SettingsEntity toEntity(String providerCode, ProviderSettings settings) {
        var entity = new SettingsEntity();
        entity.setProviderCode(providerCode);
        entity.setMinUpdateIntervalSeconds(settings.minUpdateIntervalSeconds());
        return entity;
    }
}
