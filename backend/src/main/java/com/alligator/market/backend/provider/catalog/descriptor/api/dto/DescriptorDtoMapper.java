package com.alligator.market.backend.provider.catalog.descriptor.api.dto;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.springframework.stereotype.Component;

/**
 * Маппер: DTO ⇄ доменная модель.
 */
@Component
public class DescriptorDtoMapper {

    /** Доменная модель ⇒ основной DTO. */
    public DescriptorDto toDto(ProviderDescriptor providerDescriptor) {
        return new DescriptorDto(
                providerDescriptor.providerCode(),
                providerDescriptor.displayName(),
                providerDescriptor.deliveryMode(),
                providerDescriptor.accessMethod(),
                providerDescriptor.bulkSubscription()
        );
    }

    /** Основной DTO ⇒ доменная модель. */
    public ProviderDescriptor toDomain(DescriptorDto dto) {
        return new ProviderDescriptor(
                dto.providerCode(),
                dto.displayName(),
                dto.deliveryMode(),
                dto.accessMethod(),
                dto.bulkSubscription()
        );
    }
}
