package com.alligator.market.backend.provider.profile.model;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.mapstruct.Mapper;

/**
 * Маппер встраиваемого компонента профиля провайдера {@link ProfileParamsEmbedded}
 * и доменной модели профиля провайдера рыночных данных {@link ProviderProfile}.
 */
@Mapper(componentModel = "spring")
public interface ProfileParamsEmbeddedMapper {

    /** Преобразует доменную модель во встраиваемый компонент. */
    ProfileParamsEmbedded toEmbedded(ProviderProfile profile);

    /** Преобразует встраиваемый компонент в доменную модель. */
    ProviderProfile toDomain(ProfileParamsEmbedded embedded);
}
