package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.springframework.stereotype.Component;

/**
 * Маппер: сущность ⇄ доменная модель.
 */
@Component
public class ProviderDescriptorEntityMapper {

    /** Сущность ⇒ доменная модель. */
    public ProviderDescriptor toDomain(ProviderDescriptorEntity entity) {
        return new ProviderDescriptor(
                entity.getProviderCode(),
                entity.getDisplayName(),
                entity.getDeliveryMode(),
                entity.getAccessMethod(),
                entity.isBulkSubscription()
        );
    }

    /** Преобразует доменную модель в сущность. */
    public ProviderDescriptorEntity toEntity(ProviderDescriptor providerDescriptor) {
        var entity = new ProviderDescriptorEntity();
        entity.setProviderCode(providerDescriptor.providerCode());
        entity.setDisplayName(providerDescriptor.displayName());
        entity.setDeliveryMode(providerDescriptor.deliveryMode());
        entity.setAccessMethod(providerDescriptor.accessMethod());
        entity.setBulkSubscription(providerDescriptor.bulkSubscription());
        return entity;
    }
}
