package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.model.Profile;
import org.mapstruct.Mapper;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface ProviderProfileDtoMapper {

    /** Преобразует доменную модель в DTO. */
    ProviderProfileDto toDto(Profile profile);

    /** Преобразует доменную модель и статус в DTO. */
    ProviderProfileStatusDto toStatusDto(Profile profile, ProfileStatus status);
}
