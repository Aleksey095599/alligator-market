package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.model.Profile;
import org.mapstruct.Mapper;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface ProfileDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    ProfileDto toDto(Profile profile);
}
