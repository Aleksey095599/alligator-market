package com.alligator.market.backend.provider.profile.model;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.mapstruct.Mapper;

/**
 * Маппер: доменная модель профиля провайдера ⇄ встраиваемый компонент.
 */
@Mapper(componentModel = "spring")
public interface ProviderProfileEmbeddedMapper {

    /** Преобразует доменную модель во встраиваемый компонент. */
    ProviderProfileEmbedded toEmbedded(ProviderProfile profile);

    /** Преобразует встраиваемый компонент в доменную модель. */
    ProviderProfile toDomain(ProviderProfileEmbedded embedded);
}
