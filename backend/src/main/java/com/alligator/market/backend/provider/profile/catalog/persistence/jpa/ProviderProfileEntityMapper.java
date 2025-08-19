package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.backend.provider.profile.model.ProviderProfileEmbeddedMapper;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность профиля провайдера ⇄ доменная модель.
 */
@Mapper(componentModel = "spring", uses = ProviderProfileEmbeddedMapper.class)
public interface ProviderProfileEntityMapper {

    /** Преобразует доменную модель в сущность. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profileParams", source = "profile")
    ProviderProfileEntity toEntity(ProviderProfile profile, ProviderProfileStatus status);

    /** Преобразует сущность в доменную модель. */
    @Mapping(target = ".", source = "profileParams")
    ProviderProfile toDomain(ProviderProfileEntity entity);
}
