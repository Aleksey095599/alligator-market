package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.backend.provider.profile.catalog.api.ProviderProfileStatusDto;
import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;
import org.springframework.stereotype.Component;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Component
public class ProfileDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    public ProfileDto toDto(ProviderStaticInfo providerStaticInfo) {
        return new ProfileDto(
                providerStaticInfo.profileStatus(),
                providerStaticInfo.providerCode(),
                providerStaticInfo.displayName(),
                providerStaticInfo.instrumentsSupported(),
                providerStaticInfo.deliveryMode(),
                providerStaticInfo.accessMethod(),
                providerStaticInfo.bulkSubscription(),
                providerStaticInfo.minPollMs()
        );
    }

    /** Преобразует основной DTO в доменную модель. */
    public ProviderStaticInfo toDomain(ProfileDto dto) {
        return new ProviderStaticInfo(
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
    public ProviderProfileStatusDto toStatusDto(ProviderStaticInfo providerStaticInfo, ProfileStatus status) {
        return new ProviderProfileStatusDto(
                providerStaticInfo.providerCode(),
                providerStaticInfo.displayName(),
                providerStaticInfo.instrumentsSupported(),
                providerStaticInfo.deliveryMode(),
                providerStaticInfo.accessMethod(),
                providerStaticInfo.bulkSubscription(),
                providerStaticInfo.minPollMs(),
                status
        );
    }
}
