package com.alligator.market.backend.provider.catalog.descriptor.web.dto;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.springframework.stereotype.Component;

/**
 * Маппер: DTO ⇄ доменная модель.
 */
@Component
public class DescriptorDtoMapper {

    /**
     * Доменная модель ⇒ основной DTO.
     *
     * @param providerCode Технический код провайдера.
     */
    public DescriptorDto toDto(String providerCode, ProviderDescriptor providerDescriptor) {
        return new DescriptorDto(
                providerCode,
                providerDescriptor.displayName(),
                providerDescriptor.deliveryMode(),
                providerDescriptor.accessMethod(),
                providerDescriptor.bulkSubscription()
        );
    }

    /** Основной DTO ⇒ доменная модель. */
    public ProviderDescriptor toDomain(DescriptorDto dto) {
        return new ProviderDescriptor(
                dto.displayName(),
                dto.deliveryMode(),
                dto.accessMethod(),
                dto.bulkSubscription()
        );
    }
}
