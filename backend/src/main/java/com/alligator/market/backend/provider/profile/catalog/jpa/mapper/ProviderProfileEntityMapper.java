package com.alligator.market.backend.provider.profile.catalog.jpa.mapper;

import com.alligator.market.backend.provider.profile.catalog.jpa.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.model.ProfileParamsEmbeddedMapper;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;

/**
 * Маппер сущности и доменной модели профиля провайдера рыночных данных.
 * Использует маппер {@link ProfileParamsEmbeddedMapper}.
 */
public final class ProviderProfileEntityMapper {

    private ProviderProfileEntityMapper() {
    }

    /** Преобразует доменную модель в сущность. */
    public static ProviderProfileEntity toEntity(ProviderProfile profile, ProviderProfileStatus status) {
        ProviderProfileEntity entity = new ProviderProfileEntity();
        entity.setStatus(status);
        entity.setProfile(ProfileParamsEmbeddedMapper.toEmbedded(profile));
        return entity;
    }

    /** Преобразует сущность в доменную модель. */
    public static ProviderProfile toDomain(ProviderProfileEntity entity) {
        return ProfileParamsEmbeddedMapper.toDomain(entity.getProfile());
    }
}
