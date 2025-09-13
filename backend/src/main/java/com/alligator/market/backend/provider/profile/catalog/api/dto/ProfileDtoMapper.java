package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.backend.provider.profile.catalog.api.ProviderProfileStatusDto;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.springframework.stereotype.Component;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Component
public class ProfileDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    public ProfileDto toDto(ProviderDescriptor providerDescriptor) {
        return new ProfileDto(
                providerDescriptor.profileStatus(),
                providerDescriptor.providerCode(),
                providerDescriptor.displayName(),
                providerDescriptor.instrumentsSupported(),
                providerDescriptor.deliveryMode(),
                providerDescriptor.accessMethod(),
                providerDescriptor.bulkSubscription(),
                providerDescriptor.minPollMs()
        );
    }

    /** Преобразует основной DTO в доменную модель. */
    public ProviderDescriptor toDomain(ProfileDto dto) {
        return new ProviderDescriptor(
                dto.profileStatus(),
                dto.providerCode(),
                dto.displayName(),
                dto.instrumentsSupported(),
                dto.deliveryMode(),
                dto.accessMethod(),
                dto.bulkSubscription(),
                dto.minPollMs()
        );
    }

    /** Преобразует доменную модель и статус в DTO для аудита. */
    public ProviderProfileStatusDto toStatusDto(ProviderDescriptor providerDescriptor, ProfileStatus status) {
        return new ProviderProfileStatusDto(
                providerDescriptor.providerCode(),
                providerDescriptor.displayName(),
                providerDescriptor.instrumentsSupported(),
                providerDescriptor.deliveryMode(),
                providerDescriptor.accessMethod(),
                providerDescriptor.bulkSubscription(),
                providerDescriptor.minPollMs(),
                status
        );
    }
}
