package com.alligator.market.backend.provider.profile.catalog.web.mapper;

import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileDto;
import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileStatusDto;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
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
