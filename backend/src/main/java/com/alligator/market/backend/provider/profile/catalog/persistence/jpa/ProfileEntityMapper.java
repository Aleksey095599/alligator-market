package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;
import org.springframework.stereotype.Component;

/**
 * Маппер: сущность профиля ⇄ доменная модель.
 */
@Component
public class ProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    public ProviderStaticInfo toDomain(ProfileEntity entity) {
        return new ProviderStaticInfo(
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
    public ProfileEntity toEntity(ProviderStaticInfo providerStaticInfo) {
        var entity = new ProfileEntity();
        entity.setProfileStatus(providerStaticInfo.profileStatus());
        entity.setProviderCode(providerStaticInfo.providerCode());
        entity.setDisplayName(providerStaticInfo.displayName());
        entity.setInstrumentsSupported(providerStaticInfo.instrumentsSupported());
        entity.setDeliveryMode(providerStaticInfo.deliveryMode());
        entity.setAccessMethod(providerStaticInfo.accessMethod());
        entity.setBulkSubscription(providerStaticInfo.bulkSubscription());
        entity.setMinPollMs(providerStaticInfo.minPollMs());
        return entity;
    }
}
