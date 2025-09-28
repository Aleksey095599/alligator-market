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
                entity.getProviderCode(),
                Duration.ofSeconds(entity.getMinUpdateIntervalSeconds())
        );
    }

    /** Доменная модель ⇒ сущность. */
    public ProviderSettingsEntity toEntity(ProviderSettings settings) {
        var entity = new ProviderSettingsEntity();
        entity.setMinUpdateIntervalSeconds(settings.minUpdateIntervalSec().getSeconds());
        return entity;
    }
}
