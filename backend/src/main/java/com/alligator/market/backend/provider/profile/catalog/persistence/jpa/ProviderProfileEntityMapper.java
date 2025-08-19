package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.model.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность профиля провайдера ⇄ доменная модель.
 */
@Mapper(componentModel = "spring")
public interface ProviderProfileEntityMapper {

    /** Преобразует доменную модель в сущность. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instrumentsSupported", ignore = true)
    @Mapping(target = "status", source = "status")
    ProviderProfileEntity toEntity(ProviderProfile profile, ProviderProfileStatus status);

    /** Преобразует сущность в доменную модель. */
    ProviderProfile toDomain(ProviderProfileEntity entity);
}
