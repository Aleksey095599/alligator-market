package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.springframework.stereotype.Component;

/**
 * Маппер: сущность ⇄ доменная модель.
 */
@Component
public class DescriptorEntityMapper {

    /** Сущность ⇒ доменная модель. */
    public ProviderDescriptor toDomain(DescriptorEntity entity) {
        return new ProviderDescriptor(
                entity.getDisplayName(),
                entity.getDeliveryMode(),
                entity.getAccessMethod(),
                entity.isBulkSubscription()
        );
    }

    /** Доменная модель ⇒ сущность. */
    public DescriptorEntity toEntity(String providerCode, ProviderDescriptor providerDescriptor) {
        DescriptorEntity entity = new DescriptorEntity();
        entity.setProviderCode(providerCode);
        entity.setDisplayName(providerDescriptor.displayName());
        entity.setDeliveryMode(providerDescriptor.deliveryMode());
        entity.setAccessMethod(providerDescriptor.accessMethod());
        entity.setBulkSubscription(providerDescriptor.bulkSubscription());
        return entity;
    }
}
