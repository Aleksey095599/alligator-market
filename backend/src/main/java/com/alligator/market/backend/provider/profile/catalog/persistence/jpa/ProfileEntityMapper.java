package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntityMappingConfig;
import com.alligator.market.domain.provider.profile.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность профиля ⇄ доменная модель.
 */
@Mapper(componentModel = "spring", config = BaseEntityMappingConfig.class)
public interface ProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    Profile toDomain(ProfileEntity entity);

    /** Преобразует доменную модель в сущность. */
    @Mapping(target = "id", ignore = true)
    ProfileEntity toEntity(Profile profile);
}

