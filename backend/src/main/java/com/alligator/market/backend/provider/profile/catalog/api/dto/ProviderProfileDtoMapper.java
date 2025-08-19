package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.domain.provider.model.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.mapstruct.Mapper;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface ProviderProfileDtoMapper {

    /** Преобразует доменную модель в DTO. */
    ProviderProfileDto toDto(ProviderProfile profile);

    /** Преобразует доменную модель и статус в DTO. */
    ProviderProfileStatusDto toStatusDto(ProviderProfile profile, ProviderProfileStatus status);
}
