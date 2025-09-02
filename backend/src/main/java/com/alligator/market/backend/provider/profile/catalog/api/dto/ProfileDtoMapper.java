package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.backend.provider.profile.catalog.api.ProviderProfileStatusDto;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import org.springframework.stereotype.Component;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Component
public class ProfileDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    public ProfileDto toDto(Profile profile) {
        return new ProfileDto(
                profile.profileStatus(),
                profile.providerCode(),
                profile.displayName(),
                profile.instrumentsSupported(),
                profile.deliveryMode(),
                profile.accessMethod(),
                profile.bulkSubscription(),
                profile.minPollMs()
        );
    }

    /** Преобразует основной DTO в доменную модель. */
    public Profile toDomain(ProfileDto dto) {
        return new Profile(
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
    public ProviderProfileStatusDto toStatusDto(Profile profile, ProfileStatus status) {
        return new ProviderProfileStatusDto(
                profile.providerCode(),
                profile.displayName(),
                profile.instrumentsSupported(),
                profile.deliveryMode(),
                profile.accessMethod(),
                profile.bulkSubscription(),
                profile.minPollMs(),
                status
        );
    }
}
