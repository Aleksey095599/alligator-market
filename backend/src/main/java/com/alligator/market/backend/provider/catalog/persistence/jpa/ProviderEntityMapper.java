package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntityMappingConfig;
import com.alligator.market.domain.provider.model.Provider;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: сущность провайдера ⇄ доменная модель.
 */
@Mapper(componentModel = "spring", config = BaseEntityMappingConfig.class)
public interface ProviderEntityMapper {

    /** Преобразует сущность в доменную модель. */
    Provider toDomain(ProviderEntity entity);

    /** Преобразует доменную модель в сущность. */
    @Mapping(target = "id", ignore = true)
    @InheritConfiguration(name = "ignoreBaseEntityFields")
    ProviderEntity toEntity(Provider provider);
}
