package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.profile.model.Profile;
import org.springframework.stereotype.Component;

/**
 * Маппер: сущность профиля ⇄ доменная модель.
 */
@Component
public class ProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    public Profile toDomain(ProfileEntity entity) {
        return new Profile(
                entity.getProfileStatus(),
                entity.getProviderCode(),
                entity.getDisplayName(),
                entity.getInstrumentsSupported(),
                entity.getDeliveryMode(),
                entity.getAccessMethod(),
                entity.isBulkSubscription(),
                entity.getMinPollMs()
        );
    }

    /** Преобразует доменную модель в сущность. */
    public ProfileEntity toEntity(Profile profile) {
        var entity = new ProfileEntity();
        entity.setProfileStatus(profile.profileStatus());
        entity.setProviderCode(profile.providerCode());
        entity.setDisplayName(profile.displayName());
        entity.setInstrumentsSupported(profile.instrumentsSupported());
        entity.setDeliveryMode(profile.deliveryMode());
        entity.setAccessMethod(profile.accessMethod());
        entity.setBulkSubscription(profile.bulkSubscription());
        entity.setMinPollMs(profile.minPollMs());
        return entity;
    }
}
