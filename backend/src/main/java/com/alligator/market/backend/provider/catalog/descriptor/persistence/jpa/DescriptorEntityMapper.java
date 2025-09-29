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
                entity.getDeliveryMode(),
                entity.getAccessMethod(),
                entity.isBulkSubscription()
        );
    }

    /** Обновление JPA-сущности. */
}
