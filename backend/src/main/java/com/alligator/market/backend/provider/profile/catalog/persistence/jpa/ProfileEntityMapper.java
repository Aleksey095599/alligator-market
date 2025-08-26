package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntityMappingConfig;
import com.alligator.market.domain.provider.profile.model.Profile;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность профиля ⇄ доменная модель.
 */
@Mapper(componentModel = "spring", config = BaseEntityMappingConfig.class)
public interface ProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    @Mapping(target = "profileStatus", source = "profileStatus")
    @Mapping(target = "providerCode", source = "providerCode")
    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "instrumentsSupported", source = "instrumentsSupported")
    @Mapping(target = "deliveryMode", source = "deliveryMode")
    @Mapping(target = "accessMethod", source = "accessMethod")
    @Mapping(target = "bulkSubscription", source = "bulkSubscription")
    @Mapping(target = "minPollMs", source = "minPollMs")
    Profile toDomain(ProfileEntity entity);

    /** Преобразует доменную модель в сущность. */
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    ProfileEntity toEntity(Profile profile);
}

