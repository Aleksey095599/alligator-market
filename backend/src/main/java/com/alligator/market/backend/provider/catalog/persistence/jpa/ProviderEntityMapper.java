package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.domain.provider.model.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность провайдера ⇄ доменная модель.
 */
@Mapper(componentModel = "spring")
public interface ProviderEntityMapper {

    /** Преобразует доменную модель в сущность. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "profileStatus")
    ProviderEntity toEntity(Provider provider);

    /** Преобразует сущность в доменную модель. */
    @Mapping(target = "profileStatus", source = "status")
    Provider toDomain(ProviderEntity entity);
}
