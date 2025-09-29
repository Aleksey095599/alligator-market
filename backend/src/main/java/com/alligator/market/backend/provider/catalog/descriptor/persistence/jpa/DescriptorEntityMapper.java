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
        DescriptorEmbedded descriptor = entity.getDescriptor();

        return new ProviderDescriptor(
                descriptor.getDisplayName(),
                descriptor.getDeliveryMode(),
                descriptor.getAccessMethod(),
                descriptor.isBulkSubscription()
        );
    }

    /** Доменная модель ⇒ сущность. */
    public DescriptorEntity toEntity(String providerCode, ProviderDescriptor providerDescriptor) {
        DescriptorEntity entity = new DescriptorEntity();
        entity.setProviderCode(providerCode);

        DescriptorEmbedded descriptor = new DescriptorEmbedded();
        descriptor.setDisplayName(providerDescriptor.displayName());
        descriptor.setDeliveryMode(providerDescriptor.deliveryMode());
        descriptor.setAccessMethod(providerDescriptor.accessMethod());
        descriptor.setBulkSubscription(providerDescriptor.bulkSubscription());

        entity.setDescriptor(descriptor);
        entity.setStatus(DescriptorStatus.ACTIVE); // Сохраняем дескриптор как активный
        return entity;
    }
}
