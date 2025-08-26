package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntityMappingConfig;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность профиля провайдера ⇄ доменная модель.
 */
@Mapper(componentModel = "spring", config = BaseEntityMappingConfig.class)
public interface ProviderProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    Profile toDomain(ProviderProfileEntity entity);

    /** Преобразует доменную модель в сущность. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instrumentsSupported", ignore = true)
    @Mapping(target = "status", source = "status")
    ProviderProfileEntity toEntity(Profile profile, ProfileStatus status);
}
