package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.springframework.stereotype.Component;

/**
 * Маппер: сущность профиля ⇄ доменная модель.
 */
@Component
public class ProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    public ProviderDescriptor toDomain(ProfileEntity entity) {
        return new ProviderDescriptor(
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
    public ProfileEntity toEntity(ProviderDescriptor providerDescriptor) {
        var entity = new ProfileEntity();
        entity.setProfileStatus(providerDescriptor.profileStatus());
        entity.setProviderCode(providerDescriptor.providerCode());
        entity.setDisplayName(providerDescriptor.displayName());
        entity.setInstrumentsSupported(providerDescriptor.instrumentsSupported());
        entity.setDeliveryMode(providerDescriptor.deliveryMode());
        entity.setAccessMethod(providerDescriptor.accessMethod());
        entity.setBulkSubscription(providerDescriptor.bulkSubscription());
        entity.setMinPollMs(providerDescriptor.minPollMs());
        return entity;
    }
}
