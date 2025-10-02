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

    /** Доменная модель ⇒ новая JPA-сущность. */
    public DescriptorEntity toEntity(String providerCode, ProviderDescriptor descriptor) {
        DescriptorEntity entity = new DescriptorEntity();
        entity.setProviderCode(providerCode);
        updateEntity(descriptor, entity);
        return entity;
    }

    /** Обновление JPA-сущности. */
    public void updateEntity(ProviderDescriptor descriptor, DescriptorEntity entity) {
        entity.setDisplayName(descriptor.displayName());
        entity.setDeliveryMode(descriptor.deliveryMode());
        entity.setAccessMethod(descriptor.accessMethod());
        entity.setBulkSubscription(descriptor.bulkSubscription());
    }
}
